package sweforce.vaadin.data.memcontainer

import com.vaadin.data.{Item, Container}
import scala.collection.JavaConversions._
import grizzled.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
trait ContainerIndex extends ContainerOrder with  Container.Indexed with Logging{

//  var itemIds = List[AnyRef]()

  def indexOfId(itemId: AnyRef) = itemOrder.indexOf(itemId)

  def getIdByIndex(index: Int) = {
    trace("getIdByIndex: " + index)
    getItem(itemOrder(index))
  }


  def getItemIds(startIndex: Int, numberOfItems: Int) : java.util.List[_] = {
    trace("getItemIds: %d, %d".format(startIndex, numberOfItems))
    val slice = itemOrder.slice(startIndex, startIndex + numberOfItems)
    trace("getItemIds completed: %d, %d".format(startIndex, numberOfItems))
    slice
  }

  def addItemAt(index: Int) : AnyRef = {
    trace("addItemAt: " + index)
    val itemId = addItem()
    insertItemId(index, itemId)
    return itemId
  }

//  private def insertItemId(index: Int, itemId : AnyRef) = {
//    val split = itemOrder.splitAt(index)
//    itemOrder = split._1 +: itemId +: split._2
//  }

  def addItemAt(index: Int, newItemId: AnyRef) : Item = {
    trace("addItemAt: " + index + ", to: " + newItemId)
    val item = addItem(newItemId)
    insertItemId(index, newItemId)
    return item;
  }


}
