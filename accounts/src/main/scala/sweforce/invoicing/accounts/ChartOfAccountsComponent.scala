package sweforce.invoicing.accounts

import sweforce.gui.ap.activity.AbstractActivity
import vaadin.scala.{Table, VerticalLayout}
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import com.vaadin.data.util.IndexedContainer
import sweforce.gui.ap.place.Place
import vaadin.scala._
import sweforce.gui.ap.place.token.{PlaceTokenizer, Prefix}

/**
 * The chart of account can have
 */

class ChartOfAccountsComponent {

  lazy val accounts = AccountEntry.getUkChartOfAccounts();

  lazy val accountBalances = AccountBalanceEntry.getBalances(accounts);

  object activity extends AbstractActivity {
    def start(p1: Display, p2: EventBus) {
      val container = new IndexedContainer()
      accounts.foreach(account => {
        val item = container.addItem(account.accountId)
        account.addToItem(item)
      })

      view.accountOverviewTable.setContainerDataSource(container)
      p1.setView(view)
    }
  }

  object view extends VaadinView {

    lazy val rootContainer = new VerticalLayout(width = 100 pct, height = 100 pct) {

      add(accountOverviewTable);
    }

    lazy val accountOverviewTable = new Table(width = 100 pct, height = 100 pct);

    def asComponent() = rootContainer


  }

}

@Prefix("accounts")
class AccountsPlace extends Place {

}

class Tokenizer extends PlaceTokenizer[AccountsPlace] {
  def getPlace(p1: String) = new AccountsPlace

  def getToken(p1: AccountsPlace) = ""
}
