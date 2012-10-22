package sweforce.vaadin.data.memcontainer

import com.vaadin.data.{Item, Container}
import com.vaadin.data.util.{DefaultItemSorter, ItemSorter}
import scala.collection.JavaConversions._
import sweforce.vaadin.data._

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
class InMemoryContainer(val itemPrototype: ItemPrototype, val itemIdGenerator: ItemIdGenerator, val itemSorter: ItemSorter)
  extends MapContainer with PrototypingContainer with ContainerOrder with ContainerIndex with ContainerSorting with ContainerFiltering
  with ItemSetChangeNotifier with PropertySetChangeNotifier {

  def this(itemPrototype: ItemPrototype) = this(itemPrototype, DefaultIdGenerator, new DefaultItemSorter())

//  def this(itemPrototype: ItemPrototype, sortableContainerPropertyIds: java.util.Collection[_]) = this(itemPrototype, DefaultIdGenerator, new DefaultItemSorter(),
//    sortableContainerPropertyIds)

  def this() = this(new PropertysetItemPrototype)

}
