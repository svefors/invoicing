package sweforce.vaadin.data.memcontainer

import com.vaadin.data.{Item, Container}
import com.vaadin.data.util.ItemSorter
import java.util.{Comparator, Collections}
import scala.collection.JavaConversions._
import util.Sorting
import grizzled.slf4j.Logging


/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
trait ContainerSorting extends ContainerOrder with Container.Sortable with Logging {

  val itemSorter: ItemSorter;


  private var sortableContainerPropertyIds = Iterable[Any]()

  val itemIdOrdering: Ordering[AnyRef] = new Ordering[AnyRef]() {
    def compare(x: AnyRef, y: AnyRef) = {
      trace("compare: " + x + ", to: " + y)
      itemSorter.compare(x, y)
    }
  }



  def sort(propertyId: Array[AnyRef], ascending: Array[Boolean]) {
    sortItemIds(propertyId, ascending)
    if (this.isInstanceOf[ItemSetChangeNotifier]) {
      this.asInstanceOf[ItemSetChangeNotifier].fireItemSetChange();
    }
  }

  def sort() {
    itemOrder = itemOrder.sorted(itemIdOrdering)
  }



  def sortItemIds(propertyId: Array[AnyRef], ascending: Array[Boolean]) = {
    itemSorter.setSortProperties(this, propertyId, ascending)
    sort()
    //    itemOrder.sortWith((x,y) => itemSorter.)
    //    val foo : java.util.List[AnyRef] = itemOrder.toList
    //    Collections.sort(foo, itemSorter)
    //    foo
    //    itemOrder.sortWith((x, y) => itemSorter.compare(x, y) == 1)
    //    itemOrder
    //    Sorting.quickSort(itemOrder)(itemIdOrdering)
    ////    Sorting.stableSort(itemOrder, itemIdOrdering)
  }

  /**
   * sort according to the last
   */
  def resort() = {

  }

  override def getSortableContainerPropertyIds: java.util.Collection[_] = sortableContainerPropertyIds

  def setSortableContainerPropertyIds(sortablePropertyIds: java.util.Collection[_]) = {
    this.sortableContainerPropertyIds = sortablePropertyIds
  }


}
