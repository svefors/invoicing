package sweforce.vaadin.data.provider

import com.vaadin.data.{Item, Container}
import sweforce.vaadin.data.UnsupportedOperationContainer


/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/9/12
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
trait PaginatedContainer[T]
//  extends Container.Ordered with UnsupportedOperationContainer
{

//  val dataProvider : DataProvider[T]
//
//  var paginationSize = 10;
//
//  var cache = Map[AnyRef, Item]()
//
//  var cacheSize = 250
//
//  var currentPage = 1
//
//  /*
//  must load items at the same time as item ids
//
//  resorting should sort all the items. (even the added ones)
//
//if we have loaded 30 + 10
//
//  it depends on the direction
//  need to save a page on either side
//   */
//
//  /*
//   *
//   */
//  override def getItem(itemId: Any) = {
//
//    val something = dataProvider.loadItem(itemId)
//    dataProvider.item(something)
//  }
//
//
//  /**
//   * don't use!
//   * @return
//   */
//  override def getItemIds : java.util.Collection[_] = {
//    new ItemIdCollection
//  }
//
//
//
//
//  class ItemIdCollection(val pageSize : Int) extends java.util.AbstractCollection[_] {
//
//    def iterator() : java.util.Iterator[_] = {
//
//      new java.util.Iterator[_](){
//
//        var position = 0;
//
//        var cachedItemIds = dataProvider.itemIds(position, pageSize)
//
//        def hasNext : Boolean = {
//          if (!cachedItemIds.hasNext){
//            cachedItemIds = dataProvider.itemIds(position, pageSize)
//            return cachedItemIds.hasNext
//          }
//          return true
//        }
//
//        def next() = {
//          if (!hasNext)
//            throw new NoSuchElementException
//          position =+ 1
//          cachedItemIds.next()
//        }
//
//        def remove() { throw new UnsupportedOperationException}
//      }
//    }
//
//    def size() = dataProvider.size()
//
//
//
//  }
}
