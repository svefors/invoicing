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
import com.vaadin.data.util.IndexedContainer
import sweforce.vaadin.table.EditableTable
import com.vaadin.data.{Item, Container}
import com.vaadin.data.util.converter.Converter
import java.util.{Locale, UUID}
import sweforce.vaadin.{ComboBoxOverloaded}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.ui._
import sweforce.vaadin.security.place.PlaceRequiresAuthentication
import com.vaadin.event.MouseEvents.{ClickEvent, ClickListener}
import vaadin.scala.Button
import vaadin.scala.HorizontalLayout
import vaadin.scala.VerticalLayout
import com.vaadin.ui.Component
import com.vaadin.ui.Field
import vaadin.scala.Table
import com.vaadin.ui.DefaultFieldFactory
import com.vaadin.ui
import vaadin.scala

//import com.vaadin.ui._

/**
 * The chart of account can have
 */

class ChartOfAccountsGUI {


  object activity extends AbstractActivity {
    def start(p1: Display, p2: EventBus) {
      view.accountOverviewTable.setContainerDataSource(model)
      view.addAccountButton.addListener(new com.vaadin.ui.Button.ClickListener {
        def buttonClick(event: ui.Button#ClickEvent) {

        }
      });

      view.removeAccountButton.addListener(new com.vaadin.ui.Button.ClickListener {
        def buttonClick(event: ui.Button#ClickEvent) {

        }
      });
      p1.setView(view)
    }
  }


  object view extends VaadinView {

    lazy val rootContainer = new VerticalLayout(width = 100 pct, height = 100 pct) {
      add(accountOverviewTable);
      setExpandRatio(accountOverviewTable, 1.0f)
      setComponentAlignment(accountOverviewTable, Alignment.TOP_LEFT)
      add(bottomToolbar)
      setComponentAlignment(bottomToolbar, Alignment.BOTTOM_LEFT)
    }

    lazy val bottomToolbar = new HorizontalLayout(width = 100 pct, margin = true, spacing = true) {
      setStyleName("bottomToolbar")
      add(addAccountButton)
      setComponentAlignment(addAccountButton, Alignment.MIDDLE_LEFT);
      add(removeAccountButton)
      setComponentAlignment(removeAccountButton, Alignment.MIDDLE_LEFT);
      add(searchFilter)
      setComponentAlignment(searchFilter, Alignment.MIDDLE_RIGHT);
      setExpandRatio(searchFilter, 1.0f)
    }

    lazy val addAccountButton = new Button(caption = "+");
    lazy val removeAccountButton = new Button(caption = "-");
    lazy val searchFilter = new ui.TextField(){
      setInputPrompt("Search")

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

          select.addItem(AccountType.Asset)
          select.addItem(AccountType.Debt)
          select.addItem(AccountType.Equity)
          select.addItem(AccountType.Expense)
          select.addItem(AccountType.Income)

          select.setWidth("100%")
          select
        } else if ("balance".equals(propertyId)) {
          //TODO should this move to the Editable Table and a 'readOnly' set of columns/properties?
          null
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
    this.addContainerProperty("balance", classOf[Currency], null);

    lazy val accounts = AccountEntry.getUkChartOfAccounts();


    accounts.foreach(account => {
      val item = this.addItem(account.accountId)
      item("number", account.number)
      item("name", account.name)
      item("type", account.accountType)
    })

    lazy val accountBalances = AccountBalanceEntry.getBalances(accounts);
    accountBalances.foreach(balance => {
      this.getItem(balance.accountId)("balance", balance.currency, true)
    })


    def showNewRowForAdd(){

    }
  }

}




