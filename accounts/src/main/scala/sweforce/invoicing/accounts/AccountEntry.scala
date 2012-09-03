package sweforce.invoicing.accounts

import java.util.UUID
import com.vaadin.data.util.{IndexedContainer, PropertysetItem, AbstractProperty}

import sweforce.invoicing.accounts.AccountType._
import sweforce.vaadin.scala.ItemMethods._
import sweforce.vaadin.scala.ContainerMethods._
import com.vaadin.data.{Container, Item}
import annotation.target.beanGetter
import reflect.BeanProperty
import javax.validation.constraints.{NotNull, Size}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/23/12
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */

case class AccountEntry(val accountId: UUID,
                        val number: String,
                        val name: String,
                        val accountType: AccountType) {

  @NotNull
  def getAccountId() = accountId;


  @Size(max = 4)
//  @Digits
  def getNumber() = number;

  @Size(min = 3, max = 4000)
  def getName() = name;

  @NotNull
  def getAccountType = accountType

  //  def x() = {
  //    accountId -> values()
  //  }

  def values(): Seq[Tuple2[Any, Any]] = {
    //val container = Container('itemId1 -> List('propertyId1 -> "value1", 'propertyId2 -> "value2"), 'itemId2 -> List())
    List(
      'accountId -> accountId, 'number -> number, 'name -> name, 'accountType -> accountType
    )
  }

  def asProduct() : Tuple2[Any, Seq[Tuple2[Any, Any]]] = {
    accountId -> values()
  }

  //  //  def addToItem(item: Item) = {
  //  //    item ("name", nameProperty);
  //  //    item ("number", numberProperty);
  //  //    item ("accountType", accountTypeProperty);
  //  //  }
  //
  //  def nameProperty = new FProperty[String](name, name = _)
  //
  //  def numberProperty = new FProperty[String](number, number = _)
  ////
  ////  def accountTypeProperty = new FProperty[String](accountType.toString, {
  ////    value: String => accountType = AccountType.withName(value)
  ////  })
  //
  //  def accountIdProperty = new FProperty[UUID](accountId)

}

object AccountEntry {

  //  def configureContainer(container: Container) = {
  //    container + ("name", classOf[String], null)
  //    container + ("number", classOf[String], null)
  //    container + ("accountType", classOf[String], null)
  //  }
  //
  //  def apply(accountId: UUID, number: String, name: String, accountType: AccountType) = {
  //    val entry = new AccountEntry
  //    entry.accountId = accountId
  //    entry.number = number
  //    entry.name = name
  //    entry.accountType = accountType
  //    entry
  //  }
  //
  //
  def apply(number: String, name: String, accountType: String): AccountEntry =
    AccountEntry(UUID.randomUUID(), number, name, AccountType.withName(accountType))

  def apply(number: String, name: String, accountType: AccountType): AccountEntry =
    AccountEntry(UUID.randomUUID(), number, name, accountType)

  //
  //  def apply(number: String, name: String, accountType: AccountType): AccountEntry = {
  //    val entry = AccountEntry(UUID.randomUUID(), number, name, accountType)
  //    entry
  //  }


  //  implicit def toNewItem(account: AccountEntry) = {
  //    val item = new PropertysetItem()
  //    item +("accountId", account.accountIdProperty);
  //    account.addToItem(item);
  //    item;
  //    //    item + account.accountIdProperty
  //    //    item + account.accountTypeProperty
  //    //    item + account.nameProperty
  //    //    item + account.numberProperty
  //  }

  //  implicit def newAccountEntry(number: String, name: String, accountType: AccountType) = {
  //    val entry = AccountEntry(number, name, accountType)
  //    entry
  //  }


  def getUkChartOfAccounts() = {
    def accounts = List[AccountEntry](
      AccountEntry("0010", "Freehold property", AccountType.Asset),
      AccountEntry("0020", "Plant and machiner", AccountType.Asset),
      AccountEntry("0021", "Plant and machinery depreciation", AccountType.Asset),
      AccountEntry("0030", "Office equipment", AccountType.Asset),
      AccountEntry("0031", "Office equipment depreciation", AccountType.Asset),
      AccountEntry("1200", "Bank current account", AccountType.Asset),
      AccountEntry("1210", "Bank deposit account", AccountType.Asset),
      AccountEntry("2200", "Sales VAT 20%", AccountType.Debt),
      AccountEntry("2201", "Purchase VAT", AccountType.Debt),
      AccountEntry("2205", "Sales VAT 5%", AccountType.Debt),
      AccountEntry("3200", "Profit and loss", AccountType.Equity),
      AccountEntry("4000", "Sales", AccountType.Income),
      AccountEntry("4010", "EU export sales", AccountType.Income),
      AccountEntry("4020", "Outside EU export sales", AccountType.Income),
      AccountEntry("4900", "Misc income", AccountType.Income),
      AccountEntry("4902", "Foreign exchange gain", AccountType.Income),
      AccountEntry("5000", "Materials purchased", AccountType.Expense),
      AccountEntry("7500", "Postage and carriage", AccountType.Expense),
      AccountEntry("7501", "Office stationery", AccountType.Expense),
      AccountEntry("7502", "Telephone, internet", AccountType.Expense),
      AccountEntry("7710", "Computer Expenses", AccountType.Expense)
    )
    accounts
  }


}


