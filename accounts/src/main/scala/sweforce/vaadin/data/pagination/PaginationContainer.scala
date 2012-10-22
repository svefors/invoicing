package sweforce.vaadin.data.pagination

import com.vaadin.data.Container._
import com.vaadin.data.{Property, Buffered, Container, Item}
import com.vaadin.data.util.{ItemSorter, DefaultItemSorter, AbstractContainer}
import java.util
import util.Collections
import scala.collection.JavaConversions._
/**
 * items are in the container
 */
abstract class PaginationContainer(val containerQuery: ContainerQuery, val containerWrite: ContainerWrite) extends AbstractContainer with Buffered with Sortable {

  var sortProperties = Array[AnyRef]()

  var sortAscending = Array[Boolean]()

  var filters = Seq[Filter]()

  var listeners = Seq[ItemSetChangeListener]()

  var itemCache = List[Item]()

  var propertyItemMapCache =  Map[Property[_], Item]();

  var bufferedWriteItems = List[Item]()

  var currentOffset = 0

  var currentLimit = 0

  /**
   * The item sorter which is used for sorting the container.
   */
  private var itemSorter: ItemSorter = new DefaultItemSorter

  /*
   * edited items should be in the
   *
   *           /** List of added items since last commit/rollback. */
       private final List<Item> addedItems = new ArrayList<Item>();
       /** List of modified items since last commit/rollback. */
       private final List<Item> modifiedItems = new ArrayList<Item>();
       /** List of deleted items since last commit/rollback. */
       private final List<Item> removedItems = new ArrayList<Item>();

   *
   */


  /**
   * Should return items in the buffer as well
   * @param offset
   * @param limit
   */
  def load(offset: Int, limit: Int) = {
    //load extra items on each side off the limit
    itemCache = containerQuery.loadItems(offset - 1, limit + 2, sortProperties, sortAscending, filters)
    //remember what items are in the store
    val firstItem = itemCache(1)
    val lastItem = itemCache(limit)
    itemCache = itemCache ::: bufferedWriteItems
    itemSorter.setSortProperties(this, sortProperties, sortAscending)

//    Collections.sort(itemCache, itemSorter)

    currentOffset = offset
    currentLimit = limit
    fireItemSetChange()
  }

  def getItem(itemId: Any): Item = {
      itemId match {
        case index: Int => {
          if (index > currentOffset && (index - currentOffset) <= itemCache.size) {
            return itemCache(currentOffset - index)
          } else {
            //outside of the loaded items
            //index \ currentLimit
            load(index - (index % currentLimit), currentLimit)
            getItem(index)
          }
        }
        case _ => return null
      }
    }


  def commit() {}

  def discard() {}

  var buffered = false;

  var modified = false

  def setBuffered(buffered: Boolean) {}

  def isBuffered = buffered

  def isModified = modified

  def isWriteThrough = buffered

  def setWriteThrough(writeThrough: Boolean) {
    setBuffered(writeThrough)
  }

  def isReadThrough = buffered

  def setReadThrough(readThrough: Boolean) {
    setBuffered(readThrough)
  }


  def size() = currentLimit

  /*
   * container properties... all items need to have it
   *
   */


  //  def getContainerPropertyIds : util.Collection = {
  //
  //    return null;
  //  }

//  def addItem(itemId: Any): Boolean = {
//    itemId match {
//      case x: Int => {
//
//        return true;
//      }
//      case _ => return false
//    }
//  }

  def addItem() = null

  def addContainerProperty(propertyId: Any, `type`: Class[_], defaultValue: Any) = false

  def getItemIds: util.Collection[Int] = {
    val snapShotSize = containerQuery.totalSize()
    return new util.AbstractCollection[Int] {
      def iterator() = {
        var index = -1;
        new util.Iterator[Int] {
          def next(): Int = {
            index = index + 1;
            return index;
          }

          def remove() {}

          def hasNext = index < snapShotSize
        }
      }

      def size() = snapShotSize
    }
  }



  /*
  * OVERRIDEN PUBLIC LISTENERS
  */
  override def addListener(listener: PropertySetChangeListener) = {
    super.addPropertySetChangeListener(listener)
  }

  override def removeListener(listener: PropertySetChangeListener) {
    super.removePropertySetChangeListener(listener)
  }

  override def addListener(listener: ItemSetChangeListener) {
    super.addItemSetChangeListener(listener)
  }

  override def removeListener(listener: ItemSetChangeListener) {
    super.removeItemSetChangeListener(listener)
  }


}
