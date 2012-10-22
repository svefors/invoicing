package sweforce.invoicing.accounts.gui

import sweforce.vaadin.data.memcontainer._
import sweforce.invoicing.accounts.domain.{AccountType, AccountId, AccountPropertyId}
import sweforce.invoicing.accounts.domain.AccountType._
import sweforce.invoicing.accounts.domain.AccountType.AccountType
import sweforce.invoicing.accounts.domain.VatLevel._
import sweforce.invoicing.accounts.infrastructure.{AccountsEditor, Accounts}
import sweforce.invoicing.accounts.domain.AccountPropertyId._
import scala.collection.JavaConversions._
import currency.Currency
import com.vaadin.data.Property.ValueChangeListener
import com.vaadin.data.{Container, Buffered, Item, Property}
import sweforce.invoicing.prevalence.RootStore
import sweforce.invoicing.accounts.app.AccountsFactory
import com.vaadin.data.util.{DefaultItemSorter, ItemSorter, ObjectProperty, AbstractProperty}
import collection.mutable.ListBuffer
import sweforce.invoicing.accounts.infrastructure.Accounts

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/13/12
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
class AccountsContainer(private var storeId: Any) extends DefaultContainer
with ContainerSorting with ContainerFiltering
with ItemSetChangeNotifier {

  val propertyIds = AccountPropertyId.values ++ List("balance")

  val itemSorter: ItemSorter = new DefaultItemSorter

  itemSorter.setSortProperties(this, Array[Object](), Array[Boolean]())

  /*
   Add a validator for postalCode and make it required
                    tf.addValidator(new RegexpValidator("[1-9][0-9]{4}",
                        "Postal code must be a five digit number and cannot start with a zero."));
                    tf.setRequired(true);
   */
  //  private var accounts: Accounts = _

  private var accountsEditor: AccountsEditor = new AccountsEditor(storeId)

  refresh()

  def refresh() = {
    setAccounts(RootStore.getInstance().accountSettings(storeId))
  }


  def setAccounts(accounts: Accounts) = {
    if (accounts != accountsEditor.getUncommittedState() && accounts != null) {
      accountsEditor = new AccountsEditor(storeId, accounts)
      itemOrder = accountsEditor.getUncommittedState().accountIds().toSeq
      sort()
      fireItemSetChange()
    }
  }



  setSortableContainerPropertyIds(getContainerPropertyIds)

  def getContainerPropertyIds = {
    propertyIds
  }

  class BalanceProperty(private var currency: Currency) extends ObjectProperty[Currency](currency) {
    setReadOnly(true)
  }


  class AccountsContainerProperty(val accountId: AccountId, val propertyId: AccountPropertyId)
    extends AbstractProperty[Any] {

    def getValue = {
      accountsEditor.getUncommittedState().readProperty(accountId, propertyId).asInstanceOf[Any]
    }

    def setValue(newValue: Any) {
      accountsEditor.writeProperty(accountId, propertyId, newValue)
    }

    def getType = {
      val clazz =
      propertyId match {
        case AccountPropertyId.vatLevel => {
          classOf[VatLevel]
        }
        case AccountPropertyId.accountType => {
          classOf[AccountType]
        }
        case _ => {
          classOf[String]
        }
      }
      clazz
    }

    override def isReadOnly = false

    override def setReadOnly(newStatus: Boolean) {
      throw new UnsupportedOperationException
    }
  }

  class AccountsContainerItem(val accountId: AccountId) extends Item {
    def getItemProperty(id: Any) = {
      if (id.asInstanceOf[AnyRef].eq("balance")) {
        new BalanceProperty(Currency(0, "SEK"))
      } else if (id.isInstanceOf[AccountPropertyId]) {
        new AccountsContainerProperty(accountId, id.asInstanceOf[AccountPropertyId])
      } else {
        null
      }

    }

    def getItemPropertyIds = getContainerPropertyIds

    def addItemProperty(id: Any, property: Property[_]) = throw new UnsupportedOperationException

    def removeItemProperty(id: Any) = throw new UnsupportedOperationException

  }

  def size() = accountsEditor.getUncommittedState().accountIds().size

  def containsId(itemId: Any) = accountsEditor.getUncommittedState().accountIds().contains(itemId)

  def getContainerProperty(itemId: Any, propertyId: Any) = {
    if (propertyId.asInstanceOf[AnyRef].eq("balance")) {
      new BalanceProperty(Currency(0, "SEK"))
    } else if (propertyId.isInstanceOf[AccountPropertyId] && itemId.isInstanceOf[AccountId]) {
      new AccountsContainerProperty(itemId.asInstanceOf[AccountId], propertyId.asInstanceOf[AccountPropertyId])
    } else {
      null
    }
  }

  def getType(propertyId: Any): Class[_] = {
    if (AccountPropertyId.vatLevel == propertyId) {
      return classOf[VatLevel]
    } else if (AccountPropertyId.accountType == propertyId) {
      return classOf[AccountType]
    } else if ("balance".equals(propertyId.asInstanceOf[AnyRef])) {
      return classOf[Currency]
    } else {
      return classOf[String]
    }
  }

  override def addItem(): AnyRef = {
    val itemId = AccountId()
    this.addItem(itemId)
    itemId
  }


  override def addItem(itemId: Any): Item = {
    accountsEditor.add(itemId.asInstanceOf[AccountId], null, null, null, null, null, null)
    val item = new AccountsContainerItem(itemId.asInstanceOf[AccountId])
    fireItemSetChange()
    item
  }

  override def removeItem(itemId: Any) = {
    if (accountsEditor.getUncommittedState().accountIds().contains(itemId)) {
      accountsEditor.delete(itemId.asInstanceOf[AccountId])
      fireItemSetChange
      true
    } else {
      false
    }

  }

  override def getItem(itemId: Any) = {
    new AccountsContainerItem(itemId.asInstanceOf[AccountId])
  }

  /**
   * only return the item ids for items that pass the filtering
   * @return
   */
  override def getItemIds = accountsEditor.getUncommittedState().accountIds()
}

