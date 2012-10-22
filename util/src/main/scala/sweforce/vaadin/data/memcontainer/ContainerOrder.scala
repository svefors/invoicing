package sweforce.vaadin.data.memcontainer

import com.vaadin.data.{Item, Container}
import scala.collection.JavaConversions._
import collection.mutable.ListBuffer
import grizzled.slf4j.Logging
import collection.immutable.{ListSet, SortedSet, TreeSet}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
trait ContainerOrder extends Container.Ordered with Logging {

  var itemOrder = Seq[AnyRef]()

  def getOrderedItemIds(): Seq[AnyRef] = itemOrder


  def nextItemId(itemId: Any): AnyRef = {
    getOrderedItemIds.splitAt(getOrderedItemIds().indexOf(itemId))._2.drop(1).headOption match {
      case Some(x) => return x
      case None => return null;
    }
  }

  def prevItemId(itemId: Any): AnyRef = {
    getOrderedItemIds.splitAt(getOrderedItemIds().indexOf(itemId))._1.dropRight((1)).lastOption match {
      case Some(x) => return x
      case None => return null;
    }
  }

  def firstItemId(): AnyRef = {
    getOrderedItemIds.headOption match {
      case Some(x) => x
      case None => null
    }
  }

  def lastItemId(): AnyRef = {
    getOrderedItemIds.lastOption match {
      case Some(x) => x
      case None => null
    }
  }

  def isFirstId(itemId: Any) = {
    getOrderedItemIds.headOption match {
      case Some(x) => x.equals(itemId)
      case None => false
    }
  }

  def isLastId(itemId: Any) = {
    getOrderedItemIds.lastOption match {
      case Some(x) => x.equals(itemId)
      case None => false
    }
  }

  abstract override def addItem(itemId: Any) = {
    val item = super.addItem(itemId)
    //    internalAddItemId(itemId)
    itemOrder = itemOrder ++ List(itemId.asInstanceOf[AnyRef])
    //    itemOrder.append(itemId.asInstanceOf[AnyRef])
    item
    //    addItemAfter(lastItemId(), itemId.asInstanceOf[AnyRef])
  }


  abstract override def addItem() = {
    val itemId  = addItemAfter(firstItemId())
//    sortedItemIds = sortedItemIds.+(itemId)
    itemId
  }


  def addItemAfter(previousItemId: Any): AnyRef = {
    val newItemId = super.addItem()
    val index = itemOrder.indexOf(previousItemId)
    insertItemId(index + 1, newItemId)
    return newItemId
  }

  /*
  when adding an item at a position it should keep that position until resorting has happened
   */


  def addItemAfter(previousItemId: Any, newItemId: AnyRef): Item = {
    val item = super.addItem(newItemId)
    insertItemId(itemOrder.indexOf(previousItemId) + 1, newItemId)
    return item;
  }

  protected def insertItemId(index: Int, itemId: AnyRef) = {
    itemOrder.add(index, itemId)
    val split = itemOrder.splitAt(index)
    itemOrder = (split._1 :+ itemId) ++ split._2
  }

}
