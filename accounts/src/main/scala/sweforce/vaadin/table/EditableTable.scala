package sweforce.vaadin.table

import com.vaadin.event.{ShortcutAction, ShortcutListener, ItemClickEvent}
import com.vaadin.ui._
import com.vaadin.event.ItemClickEvent.ItemClickListener
import com.vaadin.data.Container
import com.vaadin.event.ShortcutAction.{ModifierKey, KeyCode}
import scala.collection.JavaConversions._
import com.vaadin.server.Resource
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener, ValueChangeNotifier}
import com.vaadin.ui.MenuBar.Command
import com.vaadin.ui.TableFieldFactory
import scala.Some
import com.vaadin.ui.Component
import com.vaadin.ui.Table
import com.vaadin.ui.DefaultFieldFactory

/**
 * the field in the editable table has actions
 * up/left/right/down and escape
 *
 */

trait EditableTable extends Table {

  /*
   * All step actions delegates to the viewModel.
   *
   */

  def stepRight();

  def stepLeft();

  def stepUp();

  def stepDown();

  def escape();


  trait ViewModel {

    def onEditedPropertyChange(itemId : AnyRef, propertyId : AnyRef)

    def openPropertyForEditing(itemId : AnyRef, propertyId : AnyRef);

    def isPropertyInEditMode(itemId : AnyRef, propertyId : AnyRef);

    def isPropertyDirty(itemId : AnyRef, propertyId : AnyRef);

    def closePropertyForEditing(itemId : AnyRef, propertyId : AnyRef);

    def closeLastPropertyForEditing();

    def wrapFieldFactory(fieldFactory : TableFieldFactory) : TableFieldFactory;

  }

  val viewModel : ViewModel;


//  def isEditable(propertyId: AnyRef): Boolean = {
//    return true

//  }

  /*
  Editable.false + doubleclick => open that field for edit
  Editable.true + doubleclick => open that field for edit (perhaps close if not doing multieditable fields)
  Editable.true + singleclick => open that field for edit / stop editing? (behavior determined by?)

   */

  this.addItemClickListener(new ItemClickListener {
    def itemClick(event: ItemClickEvent) {
      if (event.isDoubleClick) {
        viewModel.openPropertyForEditing(event.getItemId, event.getPropertyId)
        if (!isEditable())
          setEditable(true)
      } else if (!event.isDoubleClick && isEditable) {

        viewModel.closeLastPropertyForEditing()
        setEditable(false)
      } else {
        //?
      }
      setValue(editingItemId)
    }
  })

  setTableFieldFactory(this.getTableFieldFactory)

  override def setTableFieldFactory(fieldFactory: TableFieldFactory) {
    if (fieldFactory != null)
      super.setTableFieldFactory(viewModel.wrapFieldFactory(fieldFactory))
    else
      super.setTableFieldFactory(viewModel.wrapFieldFactory(DefaultFieldFactory.get()))
  }


  var editingPropertyId: AnyRef = _;
  var editingItemId: AnyRef = _;

  class WrappedFieldFactory(val inner: TableFieldFactory) extends TableFieldFactory {
    def createOptionField(container: Container, itemId: AnyRef, propertyId: AnyRef, uiContext: Component): Option[Field[_]] = {
      if (propertyId.equals(EditableTable.this.editingPropertyId) && itemId.equals(EditableTable.this.editingItemId)) {
        val field = inner.createField(container, itemId, propertyId, uiContext)
        if (field != null){
          field.addValueChangeListener(new ValueChangeListener {
            def valueChange(event: ValueChangeEvent) {
              System.out.println("itemId: " + itemId + ", propertyId: " + propertyId + ", changed value: " + event.getProperty.getValue)
            }
          })
          return Some(field)
        }else
          return None
      } else {
        return None;
      }
    }


    def getNextEditablePropertyId(list: Array[AnyRef], propertyId: AnyRef): AnyRef = {
      if (list.last == editingPropertyId) {
        list.find(p => viewModel.isPropertyEditable(p)) match {
          case Some(nextPropertyId) => return nextPropertyId
          case None => null //shouldn't happen?
        }
      } else {
        val index = list.indexOf(editingPropertyId)
        val splits = list.splitAt(index + 1)
        val left = splits._1
        val right = splits._2
        right.find(p => viewModel.isPropertyEditable(p)) match {
          case Some(nextPropertyId) => return nextPropertyId
          case None => {
            left.find(p => viewModel.isPropertyEditable(p)) match {
              case Some(nextPropertyId) => return nextPropertyId
              case None => return null
            }
          }
        }
        return null
      }
    }

    val resource: Resource = null

    lazy val enterForward = new ForwardAction(KeyCode.ENTER)

    lazy val enterBackward = new BackwardAction(KeyCode.ENTER)

    lazy val tabForward = new ForwardAction(KeyCode.TAB)

    lazy val tabBackward = new BackwardAction(KeyCode.TAB)

    lazy val escape = new CancelFieldEdit(KeyCode.ESCAPE)

    var oldValue: Any = null;

    /*
    on cancel editing:
      revert field to old value
      setEditable to false
      make sure the
     */
    class CancelFieldEdit(val keyCode: Int) extends ShortcutListener("Undo Field Changes", resource, keyCode) {
      def handleAction(sender: Any, target: Any) {

        getContainerProperty(editingItemId, editingPropertyId).setValue(oldValue)
        setEditable(false)
        setValue(editingItemId)
      }
    }

    class ForwardAction(val keyCode: Int) extends ShortcutListener("Next Field", resource, keyCode) {
      def handleAction(sender: Any, target: Any) {
        /*
        go to the next editable field

        refresh the row cache
        and let the field factory do it's work

         */
        if (oldValue != null && !oldValue.equals(getContainerProperty(editingItemId, editingPropertyId).getValue)
        || oldValue == null && getContainerProperty(editingItemId, editingPropertyId).getValue != null){
          viewModel.onEditedPropertyChange(editingItemId, editingPropertyId)
        }
        var nextEditingPropertyId = getNextEditablePropertyId(getVisibleColumns, editingPropertyId)
        if (getVisibleColumns.indexOf(nextEditingPropertyId) <= getVisibleColumns.indexOf(editingPropertyId)) {
          editingItemId = nextItemId(editingItemId)
        }
        editingPropertyId = nextEditingPropertyId

        setValue(editingItemId)
        refreshRowCache()
      }
    }

    class BackwardAction(val keyCode: Int) extends ShortcutListener("Previous Field", resource, keyCode, ModifierKey.SHIFT) {
      def handleAction(sender: Any, target: Any) {
        if (oldValue != null && !oldValue.equals(getContainerProperty(editingItemId, editingPropertyId).getValue)){
          viewModel.onEditedPropertyChange(editingItemId, editingPropertyId)
        }
        var prevEditingPropertyId = getNextEditablePropertyId(getVisibleColumns.reverse, editingPropertyId)
        if (getVisibleColumns.indexOf(prevEditingPropertyId) >= getVisibleColumns.indexOf(editingPropertyId)) {
          editingItemId = prevItemId(editingItemId)
        }
        editingPropertyId = prevEditingPropertyId
        setValue(editingItemId)
        refreshRowCache()
      }
    }


    override def createField(container: Container, itemId: AnyRef, propertyId: AnyRef, uiContext: Component): Field[_] = {
      createOptionField(container, itemId, propertyId, uiContext) match {
        case Some(field) => {
          if (field.isInstanceOf[AbstractComponent]) {
            oldValue = container.getContainerProperty(itemId, propertyId).getValue
            field.asInstanceOf[AbstractComponent].addShortcutListener(enterForward)
            field.asInstanceOf[AbstractComponent].addShortcutListener(enterBackward)
            field.asInstanceOf[AbstractComponent].addShortcutListener(tabForward)
            field.asInstanceOf[AbstractComponent].addShortcutListener(tabBackward)
            field.asInstanceOf[AbstractComponent].addShortcutListener(escape)
          }
          field.focus()
          return field
        }
        case None => {
          return null
        }
      }
    }
  }


}



