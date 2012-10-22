package sweforce.vaadin.data

import com.vaadin.data.util.{ObjectProperty, PropertysetItem}
import scala.collection.JavaConversions._
import com.vaadin.data.{Item, Property}
import sweforce.vaadin.data
import grizzled.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/10/12
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
class PropertysetItemPrototype extends ItemPrototype with Logging {

  var defaultPropertySetItem = new PropertysetItem

  def newItem() = {
    val item = new NestedItem(new PropertysetItem)
    item
  }


  /**
   * Gets the ID's of all Properties for all items that has been or will be created by this prototype.
   * The ID's cannot be modified through the returned collection.
   *
   * @return unmodifiable collection of Property IDs
   */
  def getItemPropertyIds() = defaultPropertySetItem.getItemPropertyIds

  def getPropertyType(propertyId: Any)  : Class[_] = {
    defaultPropertySetItem.getItemProperty(propertyId).getType
  }



  override def removePrototypeItemProperty(propertyId: Any) = {
    defaultPropertySetItem.removeItemProperty(propertyId)
  }


  /**
   * Adds a new Property to all Items that has been or will be created by this prototype. The Property ID, data
   * type and default value of the new Property are given as parameters.
   *
   * must add property to ALL items previously created!
   *
   * @param propertyId
     * ID of the Property
   * @param clazz
     * Data type of the new Property
   * @param defaultValue
     * The value all created Properties are initialized to
   * @return <code>true</code> if the operation succeeded, <code>false</code>
   *         if not
   * @throws UnsupportedOperationException
   *         if the container does not support explicitly adding container
   *         properties
   */
  override def addPrototypeItemProperty[T](propertyId: Any, clazz: Class[T], defaultValue: T) : Boolean = {
    defaultPropertySetItem.addItemProperty(propertyId, new ObjectProperty[T](defaultValue, clazz))
  }


  /**
   * Ensures that:
   * All Items in the Container must have the same number of Properties.
   * All Items in the Container must have the same Property ID's (see Item.getItemPropertyIds()).
   * All Properties in the Items corresponding to the same Property ID must have the same data type.
   * @param inner
   */
  class NestedItem(val inner: Item) extends Item {


    def getItemProperty(id: Any) = {
      a_getItemProperty(id)
    }

    /**
     *
     * @param id
     * @return
     */
    def a_getItemProperty[T](id: Any) = {
      if (!defaultPropertySetItem.getItemPropertyIds.contains(id)) {
        null
      } else {
        var property = inner.getItemProperty(id)
        if (property == null) {
          trace("creating new property: %s".format(id))
          val value: T = defaultPropertySetItem.getItemProperty(id).getValue.asInstanceOf[T]
          val clazz = defaultPropertySetItem.getItemProperty(id).getType.asInstanceOf[Class[T]]
          property = new ObjectProperty(value, clazz)
          inner.addItemProperty(id, property)

        }
        trace("return property: %s".format(id))
        property
      }
    }


    //    class StupidProperty[Any](value : Any, clazz : Class[_ <: Any]) extends ObjectProperty[Any](value, clazz)


    def getItemPropertyIds(): java.util.Collection[_] = {
      val propertysetItemPrototype: PropertysetItemPrototype = PropertysetItemPrototype.this
      propertysetItemPrototype.getItemPropertyIds()
    }

    /**
     * can't add properties to the outer item. They have to be added to the inner instance
     * @param id
     * @param property
     * @return
     */
    def addItemProperty(id: Any, property: Property[_]) = throw new UnsupportedOperationException

    /**
     * can't remove properties from the outer item. They have to be added to the inner instance
     * @param id
     * @return
     */
    def removeItemProperty(id: Any) = throw new UnsupportedOperationException
  }

}
