package sweforce.vaadin.data.memcontainer

import com.vaadin.data.Container
import com.vaadin.data.Container.PropertySetChangeListener
import com.vaadin.data.Item.PropertySetChangeEvent

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
trait PropertySetChangeNotifier extends Container with Container.PropertySetChangeNotifier {

  private var listeners = Set[PropertySetChangeListener]()

  def addPropertySetChangeListener(listener: PropertySetChangeListener) = listeners = listeners + listener

  def addListener(listener: PropertySetChangeListener) = addPropertySetChangeListener(listener)

  def removePropertySetChangeListener(listener: PropertySetChangeListener) = listeners - listener

  def removeListener(listener: PropertySetChangeListener) = removePropertySetChangeListener(listener)

  def firePropertySetChange() = {
    listeners.foreach(li => {
      li.containerPropertySetChange(new Container.PropertySetChangeEvent {
        def getContainer = PropertySetChangeNotifier.this
      })
    })
  }

  abstract override def addContainerProperty(propertyId: Any, `type`: Class[_], defaultValue: Any) : Boolean = {
    if (super.addContainerProperty(propertyId, `type`, defaultValue)) {
      firePropertySetChange()
      return true
    } else {
      return false
    }
  }

  abstract override def removeContainerProperty(propertyId: Any) : Boolean = {
    if (super.removeContainerProperty(propertyId)) {
      firePropertySetChange()
      return true
    } else {
      return false
    }

  }
}
