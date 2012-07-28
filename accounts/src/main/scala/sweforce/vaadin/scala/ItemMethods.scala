package sweforce.vaadin.scala

import com.vaadin.data.{Property, Item}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/24/12
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */

class ItemMethods(val item: Item) {

  def +(propertyId: AnyRef, p: Property[_]) = {
    item.addItemProperty(propertyId, p)
  }

  def -(propertyId: AnyRef) = {
    item.removeItemProperty(propertyId)
  }


}

object ItemMethods {

  implicit def addItemMethods(item: Item) = {
    new ItemMethods(item);
  }

  implicit def removeItemMethods(itemMethods: ItemMethods) = {
    itemMethods.item
  }

}

