package sweforce.vaadin.table

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/18/12
 * Time: 10:00 PM
 * To change this template use File | Settings | File Templates.
 */
trait EditableGridViewModel {

  /**
   * returns true if the propertyId and itemId is currently being edited in the grid.
   * Can be used by TableFieldFactory to determine if a field should be generated
   * @param itemId
   * @param propertyId
   */
  def isPropertyBeingEdited(itemId : AnyRef, propertyId : AnyRef) : Boolean

  /**
   *
   * @param itemId
   * @param propertyId
   * @return
   */
  def isPropertyDirty(itemId : AnyRef, propertyId : AnyRef) : Boolean


}