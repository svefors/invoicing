package sweforce.invoicing.accounts

import sweforce.gui.ap.activity.AbstractActivity
import vaadin.scala.{Table, VerticalLayout}
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import com.vaadin.data.util.IndexedContainer
import sweforce.gui.ap.place.Place
import sweforce.gui.ap.place.token.Prefix
import com.google.gwt.place.shared.PlaceTokenizer


/**
 * The chart of account can have
 */

class ChartOfAccountsComponent {

  val accounts = AccountEntry.getUkChartOfAccounts();

  @Prefix("accounts")
  class AccountsPlace extends Place{
    class Tokenizer extends PlaceTokenizer[AccountsPlace]{
      def getPlace(p1: String) = new AccountsPlace

      def getToken(p1: ChartOfAccountsComponent.this.type#AccountsPlace) = ""
    }
  }

  object activity extends AbstractActivity {
    def start(p1: Display, p2: EventBus) {
      p1.setView(view)
    }
  }

  object view extends VerticalLayout with VaadinView {
    val accountOverviewTable = new Table()
    accountOverviewTable.setSizeFull();
    add(accountOverviewTable)
    //setExpandRatio(accountOverviewTable, 0.9)
    val container = new IndexedContainer()
    accounts.foreach( account =>
    {
      val item = container.addItem(account.accountId)
      account.addToItem(item)
    })
    accountOverviewTable.setContainerDataSource(container)
    def asComponent() = this

  }
}
