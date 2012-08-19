package sweforce.invoicing.accounts

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import sweforce.gui.ap.place.Place
import vaadin.scala._
import sweforce.gui.ap.place.token.{PlaceTokenizer, Prefix}
import reflect.BeanProperty
import sweforce.invoicing.accounts.AccountType._
import currency.Currency
import currency.Currency._
import sweforce.vaadin.scala.ItemMethods._
import com.vaadin.data.util.IndexedContainer
import sweforce.vaadin.table.EditableTable
import com.vaadin.data.{Item, Container}
import com.vaadin.data.util.converter.Converter
import java.util.{Locale, UUID}
import sweforce.vaadin.{ComboBoxOverloaded, NativeSelectOverloaded}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.ui.{Root, Field, Component, DefaultFieldFactory}

//import com.vaadin.ui._

/**
 * The chart of account can have
 */

class ChartOfAccountsComponent {


  object activity extends AbstractActivity {
    def start(p1: Display, p2: EventBus) {
      view.accountOverviewTable.setContainerDataSource(model)
      p1.setView(view)
    }
  }


  object view extends VaadinView {

    lazy val rootContainer = new VerticalLayout(width = 100 pct, height = 100 pct) {
      add(accountOverviewTable);
    }

    lazy val accountOverviewTable = {
      val table = new Table(width = 100 pct, height = 100 pct) with EditableTable;
      table.setTableFieldFactory(fieldFactory)
      table
    }

    object fieldFactory extends DefaultFieldFactory {


      override def createField(container: Container, itemId: AnyRef, propertyId: AnyRef, uiContext: Component): Field[_] = {
        if ("type".equals(propertyId)) {
          val select = new ComboBoxOverloaded
//          select.setConverterObject(accountTypeConverter.asInstanceOf[Converter[Object, _]])
          select.setNullSelectionAllowed(false)
//          select.addItem(AccountType.Asset.toString)
//          select.addItem(AccountType.Debt.toString)
//          select.addItem(AccountType.Equity.toString)
//          select.addItem(AccountType.Expense.toString)
//          select.addItem(AccountType.Income.toString)
          select.addItem(AccountType.Asset)
          select.addItem(AccountType.Debt)
          select.addItem(AccountType.Equity)
          select.addItem(AccountType.Expense)
          select.addItem(AccountType.Income)
          //          select.setValue(container.getItem(itemId).getItemProperty(propertyId).getValue.toString)
          //          select.setPropertyDataSource(container.getItem(itemId).getItemProperty(propertyId))
          //          select.setImmediate(true);
          select.setWidth("100%")
          select
        } else {
          super.createField(container, itemId, propertyId, uiContext);
        }
      }
    }

    def asComponent() = rootContainer


  }

  object accountTypeConverter extends Converter[String, AccountType] {
    def getPresentationType = {
      classOf[String]
    }

    def getModelType = {
      classOf[AccountType]
    }

    def convertToPresentation(value: AccountType.AccountType, locale: Locale): String = {
      if (value == null) {
        null;
      }
      value.toString
    }

    def convertToModel(value: String, locale: Locale): AccountType.AccountType = {
      if (value == null) {
        null;
      }
      val accountType = AccountType.withName(value)
      accountType
    }
  }

  object model extends IndexedContainer {
    this.addContainerProperty("number", classOf[String], "");
    this.addContainerProperty("name", classOf[String], "");
    this.addContainerProperty("type", classOf[AccountType], AccountType.Asset);
//    this.addContainerProperty("type", classOf[String], AccountType.Asset.toString);
    this.addContainerProperty("balance", classOf[Currency], null);

    lazy val accounts = AccountEntry.getUkChartOfAccounts();


    accounts.foreach(account => {
      val item = this.addItem(account.accountId)
      item("number", account.number)
      item("name", account.name)
      item("type", account.accountType)
//      item("type", account.accountType.toString)
    })

    lazy val accountBalances = AccountBalanceEntry.getBalances(accounts);
    accountBalances.foreach(balance => {
      this.getItem(balance.accountId)("balance", balance.currency, true)
    })
  }

}


//class ChartOfAccountsRow(@BeanProperty
//  val accountId: UUID = UUID.randomUUID()
//                          ) {
//
//  @BeanProperty
//  var number: String = _;
//
//  @BeanProperty
//  var name: String = _;
//
//  @NotNull
//  @BeanProperty
//  var accountType: AccountType = _;
//
//  @BeanProperty
//  var currency: Currency = Currency(0.toLong, "NOK")
//
//
//}

@Prefix("accounts")
class AccountsPlace extends Place {

}

class AccountsPlaceTokenizer extends PlaceTokenizer[AccountsPlace] {

  def getPlace(p1: String) = new AccountsPlace

  def getToken(p1: AccountsPlace) = ""
}
