package sweforce.vaadin.table

import com.vaadin.event.ItemClickEvent
import com.vaadin.data.Container
import com.vaadin.ui._

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 8/16/12
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */

trait EditableTable extends Table with ItemClickEvent.ItemClickListener {

//  var editableColumns = Set[String]()

  this.addListener(this)
  this.setTableFieldFactory(DefaultFieldFactory.get())

  override def setTableFieldFactory(fieldFactory: TableFieldFactory) = {
    super.setTableFieldFactory(new WrappedFieldFactory(fieldFactory));
  }

//  def editableColumn(columns: Seq[String]) {
//    columns.foreach(
//      editableColumns = editableColumns + _
//    )
//  }

  def itemClick(event: ItemClickEvent) = {
    if (event.isDoubleClick) {
      if (!this.isEditable)
        this.setEditable(true)
      editingItemId = event.getItemId
      editingPropertyId = event.getPropertyId
      this.refreshRowCache()
    } else if (!event.isDoubleClick && this.isEditable) {
      editingItemId = null;
      editingPropertyId = null;
      this.setEditable(false);
    } else {
      //?
    }
  }

  var editingPropertyId: AnyRef = _;
  var editingItemId: AnyRef = _;

  class WrappedFieldFactory(val inner: TableFieldFactory) extends TableFieldFactory {

    override def createField(container: Container, itemId: AnyRef, propertyId: AnyRef, uiContext: Component): Field[_] = {
//      if (!editableColumns.contains(propertyId)) {
//        return null;
//      }
      if (propertyId.equals(EditableTable.this.editingPropertyId) && itemId.equals(EditableTable.this.editingItemId)) {
        return inner.createField(container, itemId, propertyId, uiContext)
      } else {
        return null;
      }
    }
  }


}
