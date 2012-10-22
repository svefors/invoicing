package sweforce.vaadin.data.memcontainer

import com.vaadin.data.{Item, Container}
import scala.collection.JavaConversions._
import sweforce.vaadin.data.{ItemIdGenerator, ItemPrototype}
import grizzled.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/9/12
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
trait MapContainer extends Container with Logging  {

  private var itemMap = Map[Any, Item]()

  val itemPrototype : ItemPrototype

  val itemIdGenerator : ItemIdGenerator

  def getItem(itemId: Any) = {
    trace("start getItem: %s".format(itemId))
    val item = itemMap(itemId)
    trace("end getItem: %s".format(itemId))
    item
  }

  def size() = itemMap.size

  def containsId(itemId: Any) = itemMap.keySet.contains(itemId)

  def removeAllItems(): Boolean = {
    if (itemMap.size > 0) {
      itemMap = Map[Any, Item]()
      true
    } else {
      false
    }

  }

  def removeItem(itemId: Any) = {
    if (itemMap.contains(itemId)) {
      itemMap = itemMap - itemId
      true
    } else {
      false
    }
  }

  def addItem(itemId: Any) = {
    trace("addItem: " + itemId)
    val item = itemPrototype.createItem()
    itemMap = itemMap + (itemId -> item)
    item
  }

  /**
   * return itemId
   */
  def addItem() = {
    trace("addItem")
    val item = itemPrototype.createItem()
    val itemId = itemIdGenerator.generateId()
    itemMap = itemMap + (itemId -> item)
    itemId
  }

  def getContainerProperty(itemId: Any, propertyId: Any) = {
    trace("getContainerProperty: itemId: %s, propertyId: %s".format(itemId, propertyId))
    itemMap.get(itemId) match {
      case Some(x) => x.getItemProperty(propertyId)
      case None => null
    }
  }

  def getItemIds : java.util.Collection[_] = {
    trace("getItemIds start")
    val ids = itemMap.keySet
    trace("getItemIds completed" )
    ids
  }
}
