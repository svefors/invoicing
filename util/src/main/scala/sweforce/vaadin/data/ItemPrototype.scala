package sweforce.vaadin.data

import com.vaadin.data.{Property, Item}
import scala.collection.JavaConversions._
import com.vaadin.data.util.ObjectProperty

/**
 * All items in a container must have the same number of properties
 */
@SerialVersionUID(1l)
trait ItemPrototype extends Serializable {

  /**
   * Gets the ID's of all Properties for all items that has been or will be created by this prototype.
   * The ID's cannot be modified through the returned collection.
   *
   * @return unmodifiable collection of Property IDs
   */
  def getItemPropertyIds(): java.util.Collection[_]

  /**
   * Removes a Property specified by the given Property ID from all items that has been or will be created by this prototype.
   * Note that the Property will be removed from all Items in the Container.
   *
   * This functionality is optional.
   *
   * @param propertyId
     *            ID of the Property to remove
   * @return <code>true</code> if the operation succeeded, <code>false</code>
   *         if not
   * @throws UnsupportedOperationException
     *             if the container does not support removing container
   *             properties
   */
  def removePrototypeItemProperty(propertyId: Any): Boolean = throw new UnsupportedOperationException

  /**
   * Adds a new Property to all Items that has been or will be created by this prototype. The Property ID, data
   * type and default value of the new Property are given as parameters.
   *
   * This functionality is optional.
   *
   * @param propertyId
     *            ID of the Property
   * @param type
     *            Data type of the new Property
   * @param defaultValue
     *            The value all created Properties are initialized to
   * @return <code>true</code> if the operation succeeded, <code>false</code>
   *         if not
   * @throws UnsupportedOperationException
     *             if the container does not support explicitly adding container
   *             properties
   */
  def addPrototypeItemProperty[T](propertyId: Any, `type`: Class[T], defaultValue: T): Boolean = throw new UnsupportedOperationException

//  def addPrototypeItemProperty(propertyId: Any, `type`: Class[_], defaultValue: Any) : Boolean = throw new UnsupportedOperationException

  def createItem(): Item = {
    val item = newItem() //new NestedItem(newItem())
    item
  }

  /**
   * Gets the data type of all Properties identified by the given Property ID.
   *
   * @param propertyId
     *            ID identifying the Properties
   * @return data type of the Properties
   */
  def getPropertyType(propertyId: Any): Class[_]

  /**
   * All Items in the Container must have the same number of Properties.
   * All Items in the Container must have the same Property ID's (see Item.getItemPropertyIds()).
   * All Properties in the Items corresponding to the same Property ID must have the same data type.
   */
  def newItem(): Item



  //  def getDefaultValue(propertyId: Any): Any

//  /**
//   * All Items in the Container must have the same number of Properties.
//   * All Items in the Container must have the same Property ID's (see Item.getItemPropertyIds()).
//   * All Properties in the Items corresponding to the same Property ID must have the same data type.
//   * @param inner
//   */
//  class NestedItem(val inner: Item) extends Item {
//
//    def getItemProperty(id: Any) = {
//      val prototype: ItemPrototype = ItemPrototype.this
//      if (!prototype.getItemPropertyIds.contains(id)) {
//        null
//      } else {
//        var property = inner.getItemProperty(id)
//        if (property == null){
//          prototype.getPropertyType()
//          property =
//        }else{
//          property
//        }
//      }
//      //      val prototype: ItemPrototype = ItemPrototype.this
//      //      if (!prototype.getItemPropertyIds.contains(id)) {
//      //        null
//      //      } else {
//      //        var property = inner.getItemProperty(id)
//      //        if (property == null)
//      //          property = new ObjectProperty[_](getDefaultValue(id), getPropertyType(id))
//      //        property
//      //      }
//    }
//
//    def getItemPropertyIds(): java.util.Collection[_] = {
//      ItemPrototype.this.getItemPropertyIds
//    }
//
//    def addItemProperty(id: Any, property: Property[_]) = throw new UnsupportedOperationException
//
//    def removeItemProperty(id: Any) = throw new UnsupportedOperationException
//  }

}
