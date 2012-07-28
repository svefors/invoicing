package sweforce.vaadin.scala

import com.vaadin.data.Container

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/24/12
 * Time: 1:50 AM
 * To change this template use File | Settings | File Templates.
 */

class ContainerMethods(val container: Container)  {

  def getSomeItem(itemId: AnyRef) = {
    val item = container.getItem(itemId)
    if (item != null)
      Some(item)
    else
      None
  }


}

object ContainerMethods {



//  def getSomeItem(container: Container, itemId: AnyRef) = {
//    val item = container.getItem(itemId)
//    if (item != null)
//      Some(item)
//    else
//      None
//  }

  implicit def addContainerMethods(container : Container) = {
    new ContainerMethods(container)
  }

  implicit def removeContainerMethods(containerMethods : ContainerMethods) = {
      containerMethods.container
    }
}
