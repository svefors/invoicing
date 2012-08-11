package sweforce.invoicing.accounts

import java.util.UUID
import com.vaadin.data.util.{AbstractProperty}

import javax.validation.constraints.NotNull
import currency.Currency
import com.vaadin.data.{Container, Item}

import vaadin.scala.implicits._
import sweforce.vaadin.scala.FProperty
import sweforce.vaadin.scala.ContainerMethods._
import currency.Currency.RoundingMode
import util.Random
;


/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/23/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */

class AccountBalanceEntry() {

  @NotNull
  var accountId: UUID = UUID.randomUUID()

  var currency: Currency = Currency(0.toLong, "NOK")

  def addToItem(item: Item) = {
    item.addItemProperty("balance", new FProperty[Double](currency.toDouble))
  }

  def addToContainer(container: Container) = {
    container.getSomeItem(accountId) match {
      case Some(item: Item) =>
        addToItem(item);
        item
      case None => {
        var item = container.addItem(accountId)
        addToItem(item);
        item
      }
    }
  }
}

object AccountBalanceEntry {

  def apply(uuid: UUID, currency: Currency) {
    val balance = new AccountBalanceEntry
    balance.accountId = uuid
    balance.currency = currency
  }

  def getBalances(accounts: Traversable[AccountEntry]) {
    val random = new Random()
    val balances = accounts.map(account => {
      AccountBalanceEntry(account.accountId, Currency(random.nextLong(), "NOK", 2))
    })
    balances
  }
}



