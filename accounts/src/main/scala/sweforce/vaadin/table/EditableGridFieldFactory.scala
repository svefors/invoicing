package sweforce.vaadin.table

import com.vaadin.ui.{Component, TableFieldFactory}
import com.vaadin.data.Container

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/18/12
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
class EditableGridFieldFactory(val cellsBeingEdited : Set[EditableCellModel], val inner: TableFieldFactory) extends TableFieldFactory {

  def createField(container: Container, itemId: Any, propertyId: Any, uiContext: Component) = {
    if (cellsBeingEdited.contains(EditableCellModel(itemId, propertyId))){
      inner.createField(container, itemId, propertyId, uiContext)
    } else {
      null
    }
  }
}
