package sweforce.vaadin.table

import com.vaadin.ui.Table
import sweforce.vaadin.table.GridEditor.ViewModel
import com.vaadin.event.ItemClickEvent.ItemClickListener
import com.vaadin.event.ItemClickEvent
import com.vaadin.data.Property
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.data.util.ObjectProperty

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/20/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
class GridEditor(val viewModel : GridEditorViewModel) extends Table {

  //add
  addItemClickListener(new ItemClickListener {
    def itemClick(event: ItemClickEvent) {
      if (event.isDoubleClick){

      }
    }
  })

  viewModel.editableChangeNotifier.addValueChangeListener(new ValueChangeListener {
    def valueChange(event: ValueChangeEvent) {
      setEditable(event.getProperty.getValue.asInstanceOf[Boolean])
    }
  })

}

trait GridEditorViewModel{

  val isInEditMode : ObjectProperty[Boolean];

  def doubleClickOnItemProperty(itemId : Any, propertyId : Any)

  /**
   * register listeners for changes to editable status
   */
  val editableChangeNotifier : Property.ValueChangeNotifier


}

