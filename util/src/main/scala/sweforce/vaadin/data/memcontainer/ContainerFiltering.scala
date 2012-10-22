package sweforce.vaadin.data.memcontainer

import com.vaadin.data.{Item, Container}
import com.vaadin.data.Container.Filter
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
trait ContainerFiltering extends Container.Filterable {

  var filters = Set[Filter]()

  def addContainerFilter(filter: Filter) {
    addedItemsSinceLastFiltering = Set[Any]()
    filters = filters + filter
    if (this.isInstanceOf[ItemSetChangeNotifier]) {
      this.asInstanceOf[ItemSetChangeNotifier].fireItemSetChange();
    }

  }

  def removeContainerFilter(filter: Filter) {
    addedItemsSinceLastFiltering = Set[Any]()
    filters = filters - filter
    if (this.isInstanceOf[ItemSetChangeNotifier]) {
      this.asInstanceOf[ItemSetChangeNotifier].fireItemSetChange();
    }
  }

  def removeAllContainerFilters() {
    if (this.isInstanceOf[ItemSetChangeNotifier]) {
      this.asInstanceOf[ItemSetChangeNotifier].fireItemSetChange();
    }
    addedItemsSinceLastFiltering = Set[Any]()
    filters = Set[Filter]()
  }

  /**
   * only return the item ids for items that pass the filtering, or newly added items
   * until filtering is changed.
   *
   * @return
   */
  abstract override def getItemIds: java.util.Collection[_] = {
    return super.getItemIds.filter(id => {
      val item = super.getItem(id)
      filters.forall(filter => filter.passesFilter(id, item) || addedItemsSinceLastFiltering.contains(id))
    })
  }

  var addedItemsSinceLastFiltering = Set[Any]()

  abstract override def addItem(itemId : Any) ={
    addedItemsSinceLastFiltering = addedItemsSinceLastFiltering + itemId
    super.addItem(itemId)
  }


  abstract override def getItem(itemId: Any) = {
    val item = super.getItem(itemId)
    if (filters.forall(filter => filter.passesFilter(itemId, item)))
      item
    else
      null
  }

}
