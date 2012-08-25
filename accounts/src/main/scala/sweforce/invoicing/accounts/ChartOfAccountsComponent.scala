package sweforce.invoicing.accounts

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import sweforce.gui.ap.place.Place
import vaadin.scala._
import sweforce.gui.ap.place.token.{PlaceTokenizer, Prefix}

import sweforce.invoicing.accounts.AccountType._
import currency.Currency
import currency.Currency._
import sweforce.vaadin.scala.ItemMethods._
import sweforce.vaadin.table.EditableTable
import com.vaadin.data.util.converter.Converter
import java.util.{Locale, UUID}
import sweforce.vaadin.{ComboBoxOverloaded}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import sweforce.vaadin.security.place.PlaceRequiresAuthentication
import com.vaadin.event.MouseEvents.{ClickEvent, ClickListener}
import vaadin.scala.Button
import vaadin.scala.HorizontalLayout
import vaadin.scala.VerticalLayout
import com.vaadin.ui.Component

//import com.vaadin.ui.Field

import vaadin.scala.Table
import vaadin.scala


//import com.vaadin.ui._

/**
 * The chart of account can have
 */

class ChartOfAccountsComponent {


  object activity extends AbstractActivity {
    def start(p1: Display, p2: EventBus) {
      view.accountOverviewTable.container = model
      view.addAccountButton.clickListeners += (event => {
        System.out.println("add button clicked")
      })

      view.removeAccountButton.clickListeners += (event => {
        System.out.println("remove button clicked")
      })


      p1.setView(view)
    }
  }


  object view extends VaadinView {

    lazy val rootContainer = new VerticalLayout() {
      sizeFull()
      add(accountOverviewTable);
      expandRatio(accountOverviewTable, 1.0f)
      alignment(accountOverviewTable, Alignment.TopLeft)
      add(bottomToolbar)
      alignment(bottomToolbar, Alignment.BottomLeft)
    }

    lazy val bottomToolbar = new HorizontalLayout() {
      width = (100 pct)
      margin = true
      spacing = true
      styleName = "bottomToolbar"
      add(alignment = Alignment.MiddleLeft, component = addAccountButton)
      add(alignment = Alignment.MiddleLeft, component = removeAccountButton)
      add(alignment = Alignment.MiddleRight, component = searchFilter, ratio = 1.0f)
    }

    lazy val addAccountButton = new Button() {
      caption = "+"
    };
    lazy val removeAccountButton = new Button() {
      caption = "-"
    };
    lazy val searchFilter = new scala.TextField()() {
      prompt = ("Search")
    }


    lazy val accountOverviewTable = {
      val table = new Table() with EditableTable {
        sizeFull()
      }
      table.tableFieldFactory_=(DefaultFieldFactory)
      table
    }

    object fieldFactory extends TableFieldFactory {


      def createField(ingredients: TableFieldIngredients): Option[Field] = {
        if ("type".equals(ingredients.propertyId)) {
          val select = new ComboBox()
          //          select.setConverterObject(accountTypeConverter.asInstanceOf[Converter[Object, _]])
          select.nullSelectionAllowed = (false)

          select.addItem(AccountType.Asset)
          select.addItem(AccountType.Debt)
          select.addItem(AccountType.Equity)
          select.addItem(AccountType.Expense)
          select.addItem(AccountType.Income)

          select.width = 100 percent
          return Some(select)
        } else if ("balance".equals(ingredients.propertyId)) {
          //TODO should this move to the Editable Table and a 'readOnly' set of columns/properties?
          return None
        } else {
          return DefaultFieldFactory.createField(ingredients);
        }
      }

      override def createField(container: Container, itemId: AnyRef, propertyId: AnyRef, uiContext: Component): Field[_] = {

      }
    }

    def asComponent() = rootContainer.p


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
    this.addContainerProperty("number", classOf[String], None);
    this.addContainerProperty("name", classOf[String], None);
    this.addContainerProperty("type", classOf[AccountType], Some(AccountType.Asset));
    this.addContainerProperty("balance", classOf[Currency], None);

    lazy val accounts = AccountEntry.getUkChartOfAccounts();


    accounts.foreach(account => {
      this.addItem(account.accountId) match {
        case Some(item) => {
          item.property("number") match {
            case Some(p) => p.value = account.number
          }
          item.property("name") match {
            case Some(p) => p.value = account.name
          }
          item.property("type") match {
            case Some(p) => p.value = account.accountType
          }
          //          item("number", account.number)
          //          item("name", account.name)
          //          item("type", account.accountType)
        }
      }


    })

    lazy val accountBalances = AccountBalanceEntry.getBalances(accounts);
    accountBalances.foreach(balance => {
      item(balance.accountId) match {
        case Some(it) => it.property("balance")
        match {
          case Some(p) => {
            p.value = balance.currency
            p.readOnly = true
          }
        }
      }
    })


    def showNewRowForAdd() {

    }
  }

}

@Prefix("accounts")
//@PlaceRequiresAuthentication
class AccountsPlace extends Place {

}

object AccountsPlace {
  def apply(): AccountsPlace = new AccountsPlace
}

class AccountsPlaceTokenizer extends PlaceTokenizer[AccountsPlace] {

  def getPlace(p1: String) = new AccountsPlace

  def getToken(p1: AccountsPlace) = ""
}
