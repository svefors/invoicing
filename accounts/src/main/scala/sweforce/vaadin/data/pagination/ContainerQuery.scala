package sweforce.vaadin.data.pagination

import com.vaadin.data.Container.Filter
import com.vaadin.data.Item

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 9/3/12
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */
trait ContainerQuery {

  /**
   * the size sans filters
   * @return
   */
   def totalSize() : Int = size()

  /**
   *
   * @param offset number of records to skip
   * @param limit number of records to in collection
   * @param filters items in collection must pass filter
   * @return a List of items matching the filters.
   */
  //TODO consider sorting parameeter
  abstract def loadItems(offset : Int, limit : Int, sortProperties : Array[AnyRef], sortAscending : Array[Boolean],  filters : Seq[Filter]) : List[Item]

  abstract def loadItem(itemId : Any) : Item

  /**
   * the size when filters are applied
   * @param filter
   * @return
   */
  abstract def size(filter : Filter*) : Int

}
