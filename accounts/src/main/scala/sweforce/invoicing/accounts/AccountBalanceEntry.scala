package sweforce.invoicing.accounts

import java.util.UUID
import com.vaadin.data.util.{AbstractProperty}

import javax.validation.constraints.NotNull
import currency.Currency
import com.vaadin.data.{Container, Item}

import vaadin.scala.implicits._

import sweforce.vaadin.scala.ContainerMethods._
import currency.Currency.RoundingMode
import util.Random
import reflect.BeanProperty
import collection.mutable.Traversable
;


/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/23/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */

case class AccountBalanceEntry(val accountId: UUID, val currency: Currency) {

  @NotNull
  def getAccountId() = accountId

  def values(): Seq[Tuple2[Any, Any]] = {
    List('accountId -> accountId, 'currency -> currency)
  }

  def asProduct(): Tuple2[Any, Seq[Tuple2[Any, Any]]] = {
    accountId -> values()
  }


  //  def addToItem(item: Item) = {
  //    item.addItemProperty("balance", new FProperty[Double](currency.toDouble))
  //  }
  //
  //  def addToContainer(container: Container) = {
  //    container.getSomeItem(accountId) match {
  //      case Some(item: Item) =>
  //        addToItem(item);
  //        item
  //      case None => {
  //        var item = container.addItem(accountId)
  //        addToItem(item);
  //        item
  //      }
  //    }
  //  }


}

object AccountBalanceEntry {

  //  def apply(uuid: UUID, currency: Currency) = {
  //    val balance = new AccountBalanceEntry
  //    balance.accountId = uuid
  //    balance.currency = currency
  //    balance
  //  }


  def getBalances(accounts: Iterable[AccountEntry]): Iterable[AccountBalanceEntry] = {
    val random = new Random()
    accounts.map(account => {
      val balance: AccountBalanceEntry = AccountBalanceEntry(account.accountId, Currency(random.nextInt(10000), "NOK", 2))
      balance
    })
  }
}



