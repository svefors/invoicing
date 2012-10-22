package sweforce.vaadin.data.memcontainer

import com.vaadin.data.Container
import sweforce.vaadin.data.ItemPrototype

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
trait PrototypingContainer extends Container{

  val itemPrototype : ItemPrototype;

  def getContainerPropertyIds = itemPrototype.getItemPropertyIds

//  def addContainerProperty(propertyId: Any, clazz: Class[_], defaultValue: Any) : Boolean =
//    itemPrototype.addPrototypeItemProperty(propertyId, clazz, defaultValue)


  def addContainerProperty(propertyId: Any, clazz: Class[_], defaultValue: Any) : Boolean = {
    return itemPrototype.addPrototypeItemProperty(propertyId, clazz.asInstanceOf[Class[Any]], defaultValue)
  }

  def removeContainerProperty(propertyId: Any) : Boolean = itemPrototype.removePrototypeItemProperty(propertyId)

  def getType(propertyId: Any) : Class[_] = itemPrototype.getPropertyType(propertyId)


}
