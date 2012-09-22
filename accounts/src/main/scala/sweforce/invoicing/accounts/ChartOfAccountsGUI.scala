package sweforce.invoicing.accounts

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus

import sweforce.gui.ap.place.token.{PlaceTokenizer, Prefix}
import scala.collection.JavaConversions._
import sweforce.invoicing.accounts.AccountType._
import sweforce.invoicing.accounts.VatLevel._
import currency.Currency
import currency.Currency._

import sweforce.vaadin.table.EditableTable
import com.vaadin.data.util.converter.Converter
import java.util.{Locale, UUID}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import sweforce.vaadin.security.place.PlaceRequiresAuthentication

import com.vaadin.data.{Container, Item, Property}

import com.vaadin.ui.Button.{ClickEvent, ClickListener}
import com.vaadin.ui._
import com.vaadin.data.util.{IndexedContainer, AbstractContainer, AbstractInMemoryContainer}
import reflect.BeanProperty


/**
 * The chart of account can have
 */

class ChartOfAccountsGUI {


  object activity extends AbstractActivity with view.Presenter {
    var started = false

    def start(p1: Display, p2: EventBus) {
      if (!started) {
        view.presenter = this
        started = true
      }
      p1.setView(view)
    }

    def accountsContainer = model.accountStoreContainer

    def addAccount(accountId: AccountId) {
      //?
    }

    def removeAccount(accountId: AccountId) {
      model.accountStore.delete(accountId)
    }

    def updateAccountProperty(accountId: AccountId, propertyId: AccountPropertyId.Value, value: Any) {

      model.accountStore.set(accountId, propertyId, value)
    }
  }


  object view extends VaadinView {

    trait Presenter {
      def accountsContainer: Container with Container.Indexed with Container.Sortable with Container.Filterable

      def addAccount(accountId: AccountId)

      def removeAccount(accountId: AccountId)

      def updateAccountProperty(accountId: AccountId, property: AccountPropertyId.Value, value: Any)

    }

    @BeanProperty
    var presenter: Presenter = _;

    lazy val rootContainer = new VerticalLayout() {
      setSizeFull()
      addComponents(accountOverviewTable, bottomToolbar)
      setComponentAlignment(accountOverviewTable, Alignment.TOP_LEFT)
      setExpandRatio(accountOverviewTable, 1.0f)
      setComponentAlignment(bottomToolbar, Alignment.BOTTOM_LEFT)
    }

    /*
    table factory must know about properties
     */
    lazy val accountOverviewTable: Table with EditableTable = new Table() with EditableTable {
      setSizeFull()
      setSelectable(true)
      setImmediate(true)
      setContainerDataSource(presenter.accountsContainer)


      /*
      add a listener to each property in the container?
      - too many listeners

      override method in Editable?

       */
      addValueChangeListener(new ValueChangeListener {
        def valueChange(event: ValueChangeEvent) {
          if (event.getProperty != null && event.getProperty.getValue != null) {
            removeAccountButton.setEnabled(true)
          } else {
            removeAccountButton.setEnabled(false)
          }
        }
      })
      setVisibleColumns((List(AccountPropertyId.accountNr, AccountPropertyId.accountDescription,
        AccountPropertyId.vatLevel,
        AccountPropertyId.accountType,
        AccountPropertyId.vatCode,
        AccountPropertyId.taxCode,
        "balance")).toArray)

      setColumnHeader(AccountPropertyId.accountNr, "Nr")
      setColumnHeader(AccountPropertyId.accountDescription, "Description")
      setColumnHeader(AccountPropertyId.accountType, "Type")
      setColumnHeader(AccountPropertyId.vatLevel, "Vat level")
      setColumnHeader(AccountPropertyId.vatCode, "VAT code")
      setColumnHeader(AccountPropertyId.taxCode, "Tax code")
      setColumnHeader("balance", "Balance")
      sort(Array(AccountPropertyId.accountNr), Array(true))
      setTableFieldFactory(fieldFactory)

      override def onEditedPropertyChange(itemId: AnyRef, propertyId: AnyRef) {
        presenter.updateAccountProperty(itemId.asInstanceOf[AccountId],
          propertyId.asInstanceOf[AccountPropertyId.Value],
          getContainerProperty(itemId, propertyId))
      }

      override def isEditable(propertyId: AnyRef) = {
        AccountPropertyId.values.contains(propertyId)
      }
    }


    lazy val bottomToolbar = new HorizontalLayout() {
      setWidth("100%")
      setHeight("50px")

      setMargin(true)
      setSpacing(true)
      setStyleName("bottomToolbar")

      addComponents(addAccountButton, removeAccountButton, searchFilter)

      setComponentAlignment(addAccountButton, Alignment.MIDDLE_LEFT)
      setComponentAlignment(addAccountButton, Alignment.MIDDLE_LEFT)
      setComponentAlignment(searchFilter, Alignment.MIDDLE_RIGHT)
      setExpandRatio(searchFilter, 1.0f)

    }

    lazy val addAccountButton = new Button() {
      setCaption("+")
      addClickListener(
        new ClickListener {
          def buttonClick(event: ClickEvent) {
            val accountId = AccountId()
            accountOverviewTable.addItem(accountId)
            accountOverviewTable.setValue(accountId)
            accountOverviewTable.setEditable(true)
          }
        }
      )
    };

    lazy val removeAccountButton = new Button() {
      setCaption("-")
      setEnabled(false)
      addClickListener(
        new ClickListener {
          def buttonClick(event: ClickEvent) = {
            presenter.removeAccount(accountOverviewTable.getValue.asInstanceOf[AccountId])
            accountOverviewTable.removeItem(accountOverviewTable.getValue)
          }
        }
      )
    };
    lazy val searchFilter = new TextField() {
      setInputPrompt("Search")
    }


    object fieldFactory extends TableFieldFactory {

      val defaultFactory = DefaultFieldFactory.get()

      override def createField(container: Container, itemId: Any, propertyId: Any, uiContext: Component): Field[_] = {
        if (propertyId.equals("balance")) {
          return null
        }
        else if (propertyId == AccountPropertyId.accountType) {
          val select: ComboBox = new ComboBox() {
            setWidth("100%")
            setNullSelectionAllowed(false)
          }
          select.addItem(AccountType.Asset)
          select.addItem(AccountType.Debt)
          select.addItem(AccountType.Equity)
          select.addItem(AccountType.Expense)
          select.addItem(AccountType.Income)
          return select
        } else if(propertyId == AccountPropertyId.vatLevel){
          val select: ComboBox = new ComboBox() {
            setWidth("100%")
            setNullSelectionAllowed(true)
          }
          select.setItemCaptionPropertyId("caption")
          select.addContainerProperty("caption", classOf[String], "")
          select.addItem(VatLevel.Normal).getItemProperty("caption").setValue("Normal")
          select.addItem(VatLevel.Reduced).getItemProperty("caption").setValue("Reduced")
          select.addItem(VatLevel.FurtherReduced).getItemProperty("caption").setValue("Further Reduced")
          return select
        } else {
          return defaultFactory.createField(container, itemId, propertyId, uiContext)
        }
      }

    }

    def asComponent() = rootContainer


  }

  //  object accountTypeConverter extends Converter[String, AccountType] {
  //    def getPresentationType = {
  //      classOf[String]
  //    }
  //
  //    def getModelType = {
  //      classOf[AccountType]
  //    }
  //
  //    def convertToPresentation(value: AccountType.AccountType, locale: Locale): String = {
  //      if (value == null) {
  //        null;
  //      }
  //      value.toString
  //    }
  //
  //    def convertToModel(value: String, locale: Locale): AccountType.AccountType = {
  //      if (value == null) {
  //        null;
  //      }
  //      val accountType = AccountType.withName(value)
  //      accountType
  //    }
  //  }

  object model extends Property.ValueChangeListener {
    lazy val accountStore = AccountStore.baskontoplan()

    var commandBuffer = List[AccountStoreCommand]()


    def valueChange(event: ValueChangeEvent) {

    }

    lazy val accountStoreContainer = new IndexedContainer() {
      addContainerProperty(AccountPropertyId.accountId, classOf[AccountId], null)
      addContainerProperty(AccountPropertyId.accountNr, classOf[String], null)
      addContainerProperty(AccountPropertyId.accountDescription, classOf[String], null)
      addContainerProperty(AccountPropertyId.accountType, classOf[AccountType], AccountType.Asset)
      addContainerProperty(AccountPropertyId.vatLevel, classOf[VatLevel], null)
      addContainerProperty(AccountPropertyId.vatCode, classOf[String], null)
      addContainerProperty(AccountPropertyId.taxCode, classOf[String], null)
      addContainerProperty("balance", classOf[Currency], Currency(0, "SEK"))
      accountStore.accountIds().foreach(accountId => {
        addItem(accountId)
      })
      accountStore.accountMap.foreach({
        case (propertyId, propertyMap) =>
          propertyMap.foreach({
            case (accountId, value) =>
              getContainerProperty(accountId, propertyId).setValue(value)
          })
      })
    }

  }

}




