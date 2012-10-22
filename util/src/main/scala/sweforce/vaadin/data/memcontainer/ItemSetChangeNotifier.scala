package sweforce.vaadin.data.memcontainer

import com.vaadin.data.Container
import com.vaadin.data.Container.{ItemSetChangeEvent, ItemSetChangeListener}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
trait ItemSetChangeNotifier extends Container with Container.ItemSetChangeNotifier{

  private var listeners = Set[ItemSetChangeListener]()

  def addItemSetChangeListener(listener: ItemSetChangeListener) {
    listeners = listeners + listener
  }

  def addListener(listener: ItemSetChangeListener) {
    listeners = listeners + listener
  }

  def removeItemSetChangeListener(listener: ItemSetChangeListener) {
    listeners = listeners - listener
  }

  def removeListener(listener: ItemSetChangeListener) {
    listeners = listeners - listener
  }


  abstract override def addItem() = {
    val itemId = super.addItem()
    fireItemSetChange()
    itemId
  }


  abstract override def removeAllItems()= {
    if (super.removeAllItems()){
      fireItemSetChange()
      true
    }else{
      false
    }
  }

  abstract override def removeItem(itemId: Any) = {
    if (super.removeItem(itemId)){
      fireItemSetChange()
      true
    }else{
      false
    }
  }


  def fireItemSetChange() = {
    listeners.foreach(li =>{
      li.containerItemSetChange(new ItemSetChangeEvent {
        def getContainer = ItemSetChangeNotifier.this
      })
    })
  }


}
