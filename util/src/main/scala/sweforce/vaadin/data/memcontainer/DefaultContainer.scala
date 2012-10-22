package sweforce.vaadin.data.memcontainer

import com.vaadin.data.{Item, Container}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/14/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
trait DefaultContainer extends Container{

  def addContainerProperty(propertyId: Any, `type`: Class[_], defaultValue: Any) : Boolean = throw new UnsupportedOperationException

  def removeContainerProperty(propertyId: Any) : Boolean = throw new UnsupportedOperationException

  def removeAllItems() : Boolean = throw new UnsupportedOperationException

  def addItem() : AnyRef = {
    throw new UnsupportedOperationException
//    val itemId = AccountId()
//    this.addItem(itemId)
//    itemId
  }


  def addItem(itemId: Any) : Item = {
    throw new UnsupportedOperationException
//    accountsEditor.add(itemId.asInstanceOf[AccountId], null, null, null, null, null, null)
//    new AccountsContainerItem(itemId.asInstanceOf[AccountId])
  }

  def removeItem(itemId: Any) : Boolean = {
    throw new UnsupportedOperationException
//    if (accountsEditor.state.accountIds().contains(itemId)){
//      accountsEditor.delete(itemId.asInstanceOf[AccountId])
//      true
//    } else{
//      false
//    }

  }

  def getItem(itemId: Any) : Item = {
    throw new UnsupportedOperationException
//    new AccountsContainerItem(itemId.asInstanceOf[AccountId])
  }

  def getItemIds() : java.util.Collection[_] = {
    throw new UnsupportedOperationException
  }

}
