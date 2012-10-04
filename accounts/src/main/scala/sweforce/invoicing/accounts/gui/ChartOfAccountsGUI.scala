package sweforce.invoicing.accounts.gui


import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus

import sweforce.gui.ap.place.token.{PlaceTokenizer, Prefix}
import scala.collection.JavaConversions._
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

import sweforce.invoicing.accounts.domain.AccountPropertyId._
import sweforce.invoicing.accounts.domain.AccountType._
import sweforce.invoicing.accounts.domain.VatLevel._
import sweforce.invoicing.accounts.infrastructure._

import sweforce.invoicing.prevalence.{RootStoreUnitOfWork, RootStore}
import sweforce.invoicing.accounts.app.AccountsFactory
import sweforce.invoicing.accounts.domain.{VatLevel, AccountType, AccountPropertyId, AccountId}
import sweforce.invoicing.accounts.domain.AccountPropertyId.AccountPropertyId
import sweforce.invoicing.accounts.domain.AccountType.AccountType
import sweforce.invoicing.accounts.domain.VatLevel.VatLevel


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
      //TODO consider publishing event to the local bus after update
      val accountsEditor = AccountsEditorRepository.load(AccountsFactory.accountStoreId)
      val uow = new RootStoreUnitOfWork
      uow.registerStateEditor(accountsEditor, AccountsEditorRepository)
      accountsEditor.delete(accountId)
      uow.commit()
      //      RootStoreCommandBus.dispatch(new PrevaylerAccountStoreCommandHandler().createPrevaylerTransaction(DeleteAccount(AccountsFactory.accountStoreId, accountId)))
    }

    def updateAccountProperty(accountId: AccountId, propertyId: AccountPropertyId.Value, value: Any) {
      val accountsEditor = AccountsEditorRepository.load(AccountsFactory.accountStoreId)
      val uow = new RootStoreUnitOfWork
      uow.registerStateEditor(accountsEditor, AccountsEditorRepository)
      accountsEditor.writeProperty(accountId, propertyId, value)
      uow.commit()
//      RootStoreCommandBus.dispatch(new PrevaylerAccountStoreCommandHandler().createPrevaylerTransaction(AccountPropertyCommand(AccountsFactory.accountStoreId, accountId, propertyId, value)))
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
          getContainerProperty(itemId, propertyId).getValue)
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
      setComponentAlignment(removeAccountButton, Alignment.MIDDLE_LEFT)
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
        } else if (propertyId == AccountPropertyId.vatLevel) {
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
          val field = defaultFactory.createField(container, itemId, propertyId, uiContext)
          if (field.isInstanceOf[TextField])
            field.asInstanceOf[TextField].setNullRepresentation("")
          return field
        }
      }

    }

    def asComponent() = rootContainer


  }

  /*
 should
  */

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
//    lazy val accountStoreRead = RootStore.getInstance().loadReadStore(AccountsFactory.accountStoreId)
    lazy val accounts = RootStore.getInstance().accountSettings(AccountsFactory.accountStoreId)
//    var commandBuffer = List[AccountStoreCommand]()


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
      accounts.accountIds().foreach(accountId => {
        addItem(accountId)
        getContainerPropertyIds.foreach(propertyId => {
          getContainerProperty(accountId, propertyId).setValue(accounts.readProperty(accountId, propertyId.asInstanceOf[AccountPropertyId]))
        })
      })
      addContainerProperty("balance", classOf[Currency], Currency(0, "SEK"))
      //      accountStoreRead.accountMap.foreach({
      //        case (propertyId, propertyMap) =>
      //          propertyMap.foreach({
      //            case (accountId, value) =>
      //              getContainerProperty(accountId, propertyId).setValue(value)
      //          })
      //      })
    }

  }

}




