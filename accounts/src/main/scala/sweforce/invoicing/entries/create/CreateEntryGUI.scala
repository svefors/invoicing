package sweforce.invoicing.entries.create

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import com.vaadin.ui._
import sweforce.invoicing.accounts.{AccountPropertyId, AccountId}
import currency.Currency
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Table
import com.vaadin.ui.TabSheet
import com.vaadin.data.Container

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 9/19/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
class CreateEntryGUI {

  object activity extends AbstractActivity with view.Presenter {
    def start(p1: Display, p2: EventBus) {
      p1.setView(view)
    }

    //    def storeTrans
    def getAccountsComboboxDatasource() = {

    }
  }


  object view extends VaadinView {

    trait Presenter {
      def getAccountsComboboxDatasource(): Container
    }

    lazy val rootLayout = new VerticalLayout() {
      setSizeFull()
      addComponent(tabsheet)
      setExpandRatio(tabsheet, 1.0f)
      setMargin(true)
    }
    lazy val tabsheet = new TabSheet {
      setWidth("100%")
      setHeight("100%")
      addTab(expense, "Expense")
      addTab(income, "Income")
      addTab(other, "Other/Manual")
    }

    lazy val expense = new VerticalLayout() {
      setSizeFull()
      addComponent(new Label("Expense"))
    }

    lazy val income = new VerticalLayout() {
      setSizeFull()
      addComponent(new Label("Income"))
    }

    lazy val other = new VerticalLayout() {
      setSizeFull()
      setSpacing(true)
      setMargin(true)
      val accountPostings = new Table() {
        setWidth("100%")

      }
      accountPostings.addContainerProperty(AccountPropertyId.accountNr, classOf[String], "")
      accountPostings.addContainerProperty(AccountPropertyId.accountDescription, classOf[String], null)
      accountPostings.addContainerProperty("Debit", classOf[Currency], null)
      accountPostings.addContainerProperty("Credit", classOf[Currency], null)
      accountPostings.setColumnExpandRatio(AccountPropertyId.accountDescription, 0.5f)
      addComponent(accountPostings)
      setComponentAlignment(accountPostings, Alignment.TOP_LEFT)
      setExpandRatio(accountPostings, 1.0f)
      val toolbar = new HorizontalLayout() {
        setSizeUndefined()
        setWidth("100%")

        addComponent(new Button("+"))
        addComponent(new Button("-"))
        addComponent(new Button("balance"))
      }
      addComponent(toolbar)
      setComponentAlignment(toolbar, Alignment.BOTTOM_LEFT)

      val bottomTransactionData = new VerticalLayout {
        setSizeUndefined()
        setSpacing(true)
        setMargin(true)
        addComponent(new Label("description"))
      }
      addComponent(bottomTransactionData)
      setComponentAlignment(bottomTransactionData, Alignment.BOTTOM_LEFT)
    }


    def asComponent() = rootLayout
  }


}
