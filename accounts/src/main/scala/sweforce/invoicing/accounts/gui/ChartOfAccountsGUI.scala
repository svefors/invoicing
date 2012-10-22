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
import sweforce.invoicing.accounts.infrastructure._

import sweforce.invoicing.prevalence.{RootStoreUnitOfWork}
import sweforce.invoicing.accounts.app.AccountsFactory
import sweforce.invoicing.accounts.domain.{VatLevel, AccountType, AccountPropertyId, AccountId}

import grizzled.slf4j.Logging
import com.vaadin.data.validator.RegexpValidator


/**
 * The chart of account can have
 */

class ChartOfAccountsGUI extends Logging {


  object activity extends AbstractActivity with view.Presenter {
    var started = false

    def start(p1: Display, p2: EventBus) {
      if (!started) {
        view.presenter = this
        started = true
      }
      p1.setView(view)
    }

    lazy val p_accountsContainer = {
      new AccountsContainer(AccountsFactory.accountStoreId)
    }

    def accountsContainer = p_accountsContainer

    def addAccount(accountId: AccountId) {
      view.accountOverviewTable.addItem(accountId)
    }

    def removeAccount(accountId: AccountId) {
      //TODO consider publishing event to the local bus after update
      val accountsEditor = AccountsEditorRepository.load(AccountsFactory.accountStoreId)
      val uow = new RootStoreUnitOfWork
      uow.registerStateEditor(accountsEditor, AccountsEditorRepository)
      accountsEditor.delete(accountId)
      uow.commit()
      accountsContainer.refresh()
    }

    def updateAccountProperty(accountId: AccountId, propertyId: AccountPropertyId.Value, value: Any) {
      val accountsEditor = AccountsEditorRepository.load(AccountsFactory.accountStoreId)
      val uow = new RootStoreUnitOfWork
      uow.registerStateEditor(accountsEditor, AccountsEditorRepository)
      accountsEditor.writeProperty(accountId, propertyId, value)
      uow.commit()
      accountsContainer.refresh()
    }
  }




  object view extends VaadinView {

    trait Presenter {
      def accountsContainer: Container  with Container.Sortable with Container.Filterable

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
      setComponentAlignment(bottomToolbar, Alignment.BOTTOM_LEFT)
      setExpandRatio(accountOverviewTable, 1.0f)
    }

    /*
    table can't be configured correctly without a container that has the correct container properties
    view model can't have dependency on view


     */

    lazy val accountOverviewTable: Table with EditableTable = new Table() with EditableTable {
      setSizeFull()
      setSelectable(true)
      setImmediate(true)
      setContainerDataSource(presenter.accountsContainer)
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

      //TODO MOVE!
      override def onEditedPropertyChange(itemId: AnyRef, propertyId: AnyRef) {
        presenter.updateAccountProperty(itemId.asInstanceOf[AccountId],
          propertyId.asInstanceOf[AccountPropertyId.Value],
          getContainerProperty(itemId, propertyId).getValue)
      }
      //TODO Move this?
      override def isEditable(propertyId: AnyRef) = {
        AccountPropertyId.values.contains(propertyId)
      }

      setTableFieldFactory(fieldFactory)

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
            accountOverviewTable.editingPropertyId = AccountPropertyId.accountNr
            accountOverviewTable.editingItemId = accountId
            accountOverviewTable.setEditable(true)
          }
        }
      )
    };

    lazy val removeAccountButton: Button = new Button() {
      setCaption("-")
      setEnabled(false)
      addClickListener(
        new ClickListener {
          def buttonClick(event: ClickEvent) = {
            presenter.removeAccount(accountOverviewTable.getValue.asInstanceOf[AccountId])
          }
        }
      )

      accountOverviewTable.addValueChangeListener(new ValueChangeListener {
        def valueChange(event: ValueChangeEvent) {
          if (event.getProperty != null && event.getProperty.getValue != null) {
            setEnabled(true)
          } else {
            setEnabled(false)
          }
        }
      })
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
            addItem(AccountType.Asset)
            addItem(AccountType.Debt)
            addItem(AccountType.Equity)
            addItem(AccountType.Expense)
            addItem(AccountType.Income)
          }

          return select
        } else if (propertyId == AccountPropertyId.vatLevel) {
          val select: ComboBox = new ComboBox() {
            setWidth("100%")
            setNullSelectionAllowed(true)
            setItemCaptionPropertyId("caption")
            addContainerProperty("caption", classOf[String], "")
            addItem(VatLevel.Normal).getItemProperty("caption").setValue("Normal")
            addItem(VatLevel.Reduced).getItemProperty("caption").setValue("Reduced")
            addItem(VatLevel.FurtherReduced).getItemProperty("caption").setValue("Further Reduced")
          }
          return select
        } else {
          val field = defaultFactory.createField(container, itemId, propertyId, uiContext)
          if (field.isInstanceOf[TextField]){
            val tf = field.asInstanceOf[TextField]
//            tf.setNullRepresentation("")
//            tf.addValidator(new RegexpValidator("[1-9][0-9]{4}",
//              "Postal code must be a five digit number and cannot start with a zero."));
//            tf.setRequired(true)
          }
          return field
        }
      }

    }

    def asComponent() = rootContainer


  }

  /*

 Signal to Noice Ratio. hide layout concerns
 Increase Testability
 - presenter is allowed to update containers after invocation of business logic method (maybe to revert)
 - view should ask presenter for validators
 - view should ask presenter for eventhandlers/listeners?

 -
 - move logic away from vaadin


  */
  //  object model extends Property.ValueChangeListener {
  //
  ////    lazy val accounts = RootStore.getInstance().accountSettings(AccountsFactory.accountStoreId)
  //
  //    def valueChange(event: ValueChangeEvent) {
  //
  //    }
  //
  //
  ////    lazy val accountStoreContainer : InMemoryContainer = new InMemoryContainer(){
  ////      addContainerProperty(AccountPropertyId.accountId, classOf[AccountId], null)
  ////      addContainerProperty(AccountPropertyId.accountNr, classOf[String], null)
  ////      addContainerProperty(AccountPropertyId.accountDescription, classOf[String], null)
  ////      addContainerProperty(AccountPropertyId.accountType, classOf[AccountType], AccountType.Asset)
  ////      addContainerProperty(AccountPropertyId.vatLevel, classOf[VatLevel], null)
  ////      addContainerProperty(AccountPropertyId.vatCode, classOf[String], null)
  ////      addContainerProperty(AccountPropertyId.taxCode, classOf[String], null)
  ////
  ////////    }
  //////    lazy val accountStoreContainer = new IndexedContainer() {
  //////      addContainerProperty(AccountPropertyId.accountId, classOf[AccountId], null)
  //////      addContainerProperty(AccountPropertyId.accountNr, classOf[String], null)
  //////      addContainerProperty(AccountPropertyId.accountDescription, classOf[String], null)
  //////      addContainerProperty(AccountPropertyId.accountType, classOf[AccountType], AccountType.Asset)
  //////      addContainerProperty(AccountPropertyId.vatLevel, classOf[VatLevel], null)
  //////      addContainerProperty(AccountPropertyId.vatCode, classOf[String], null)
  //////      addContainerProperty(AccountPropertyId.taxCode, classOf[String], null)
  ////      trace("start filling container")
  ////      accounts.accountIds().foreach(accountId => {
  ////        addItem(accountId)
  ////        getContainerPropertyIds.foreach(propertyId => {
  ////          getContainerProperty(accountId, propertyId).setValue(accounts.readProperty(accountId, propertyId.asInstanceOf[AccountPropertyId]))
  ////        })
  ////      })
  ////      trace("end filling container")
  ////      addContainerProperty("balance", classOf[Currency], Currency(0, "SEK"))
  ////      setSortableContainerPropertyIds(getContainerPropertyIds)
  ////
  ////    }

  //  }

}

//class AccountsSettingsTable extends Table with EditableTable{
//
//  def layout(){
//    //layout
//    setSizeFull()
//
////    //logic or behavior
////    setSelectable(true)
////    setImmediate(true)
//  }
//
//
//}



