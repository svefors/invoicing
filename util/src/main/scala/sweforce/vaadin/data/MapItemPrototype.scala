package sweforce.vaadin.data

import com.vaadin.data.util.{ObjectProperty, PropertysetItem}
import com.vaadin.data.{Property, Item}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/12/12
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
class MapItemPrototype extends ItemPrototype {

  var defaultPropertySetItem = new PropertysetItem

  private var properties = scala.collection.mutable.Map[Item, scala.collection.mutable.Map[Any, Any]]()

  var readonlyProperties = Set[MapItemProperty]()

  /**
   * Gets the ID's of all Properties for all items that has been or will be created by this prototype.
   * The ID's cannot be modified through the returned collection.
   *
   * @return unmodifiable collection of Property IDs
   */
  def getItemPropertyIds() = defaultPropertySetItem.getItemPropertyIds

  /**
   * Gets the data type of all Properties identified by the given Property ID.
   *
   * @param propertyId
     * ID identifying the Properties
   * @return data type of the Properties
   */
  def getPropertyType(propertyId: Any): Class[_] = {
    defaultPropertySetItem.getItemProperty(propertyId).getType
  }

  class MapItemProperty(val item: Item, val propertyId: Any) extends Property[Object] {
    def getValue = properties.get(item) match {
      case Some(item) => item.get(propertyId) match {
        case Some(value) => value.asInstanceOf[AnyRef]
        case None => {
          defaultPropertySetItem.getItemProperty(propertyId).getValue.asInstanceOf[AnyRef]
        }
      }
      case None => null
    }

    def setValue(newValue: Any) {
      properties.get(item) match {
        case Some(map) => {
          if (newValue == null) {
            properties(item).put(propertyId, newValue)
//            properties(item) = properties(item) - (propertyId)
          } else {
            properties(item).remove(propertyId)
//            properties(item) = properties(item) + (propertyId -> newValue)
          }
        }
        case None => {
          //what to do?
        }
      }
    }

    def getType = defaultPropertySetItem.getItemProperty(propertyId).getType.asInstanceOf[Class[_ <: Object]]

    def isReadOnly = readonlyProperties.contains(this)

    def setReadOnly(newStatus: Boolean) {
      if (newStatus)
        readonlyProperties = readonlyProperties + this
      else
        readonlyProperties = readonlyProperties - this
    }
  }

  /**
   * All Items in the Container must have the same number of Properties.
   * All Items in the Container must have the same Property ID's (see Item.getItemPropertyIds()).
   * All Properties in the Items corresponding to the same Property ID must have the same data type.
   */
  def newItem() = {
    val item = new MapItem()
    properties.put(item, scala.collection.mutable.Map[Any, Any]())
    item
  }

  class MapItem() extends Item {

    def getItemProperty(id: Any) = {
      if (getItemPropertyIds.contains(id))
        new MapItemProperty(this, id)
      else
        null
    }

    def getItemPropertyIds = defaultPropertySetItem.getItemPropertyIds

    def addItemProperty(id: Any, property: Property[_]) = throw new UnsupportedOperationException

    def removeItemProperty(id: Any) = throw new UnsupportedOperationException
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
}
