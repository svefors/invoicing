package sweforce.vaadin.data

import com.vaadin.data.{Item, Container}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/9/12
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
trait UnsupportedOperationContainer extends Container{
  def getItem(itemId: Any) = null

  def getContainerPropertyIds = null

  def getItemIds = null

  def getContainerProperty(itemId: Any, propertyId: Any) = null

  def getType(propertyId: Any) = null

  def size() = 0

  def containsId(itemId: Any) = false

  def addItem(itemId: Any) : Item = throw new UnsupportedOperationException

  def addItem() : AnyRef = throw new UnsupportedOperationException

  def removeItem(itemId: Any) : Boolean = throw new UnsupportedOperationException

  def addContainerProperty(propertyId: Any, `type`: Class[_], defaultValue: Any) : Boolean = throw new UnsupportedOperationException

  def removeContainerProperty(propertyId: Any) : Boolean = throw new UnsupportedOperationException

  def removeAllItems() : Boolean = throw new UnsupportedOperationException
}
