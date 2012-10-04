package sweforce.invoicing.entries.create

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import com.vaadin.ui._
import sweforce.invoicing.accounts.domain.{VatLevel, AccountType, AccountId, AccountPropertyId}
import currency.Currency
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Table
import com.vaadin.ui.TabSheet
import com.vaadin.data.Container
import sweforce.invoicing.prevalence.RootStore
import sweforce.invoicing.accounts.app.AccountsFactory
import com.vaadin.data.util.IndexedContainer
import sweforce.invoicing.accounts.domain.AccountType._
import sweforce.invoicing.accounts.domain.AccountType.AccountType
import sweforce.invoicing.accounts.domain.VatLevel._
import sweforce.invoicing.accounts.domain.AccountPropertyId._
import scala.collection.JavaConversions._
import sweforce.vaadin.table.EditableTable
import sweforce.invoicing.accounts.domain.VatLevel.VatLevel
import sweforce.invoicing.accounts.infrastructure.AccountsRepository
import reflect.BeanProperty
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.ui.Button.{ClickEvent, ClickListener}
import java.util.UUID

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
      view.presenter = this
      p1.setView(view)
    }

    //    def storeTrans
    def getAccountsComboboxDatasource() = {
      val accounts = RootStore.getInstance().accountSettings(AccountsFactory.accountStoreId)
      val container = new IndexedContainer() {
        addContainerProperty(AccountPropertyId.accountId, classOf[AccountId], null)
        addContainerProperty(AccountPropertyId.accountNr, classOf[String], null)
        addContainerProperty(AccountPropertyId.accountDescription, classOf[String], null)

        accounts.accountIds().foreach(accountId => {
          addItem(accountId)
          getContainerProperty(accountId, AccountPropertyId.accountNr)
            .setValue(accounts.readProperty(accountId, AccountPropertyId.accountNr))
          getContainerProperty(accountId, AccountPropertyId.accountDescription)
                      .setValue(accounts.readProperty(accountId, AccountPropertyId.accountDescription))
        })


      }
      container
    }

    def getTransactionContainer() = {
      val container = new IndexedContainer() {
        addContainerProperty(AccountPropertyId.accountNr, classOf[String], null)
        addContainerProperty(AccountPropertyId.accountDescription, classOf[String], null)
        addContainerProperty("Debit", classOf[Currency], null)
        addContainerProperty("Credit", classOf[Currency], null)
      }
      container
    }
  }


  object view extends VaadinView {

    trait Presenter {
      def getAccountsComboboxDatasource(): Container

      def getTransactionContainer(): Container
    }

    @BeanProperty
    var presenter: Presenter = _;

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

    lazy val accountPostings: Table with EditableTable = new Table() with EditableTable {
      setWidth("100%")
      setContainerDataSource(presenter.getTransactionContainer())
      setColumnExpandRatio(AccountPropertyId.accountDescription, 0.5f)
      setColumnExpandRatio("Debit", 0.2f)
      setColumnExpandRatio("Credit", 0.2f)
      setTableFieldFactory(fieldFactory)
    }

    lazy val addTransactionRowButton = new Button() {
      setCaption("+")
      addClickListener(
        new ClickListener {
          def buttonClick(event: ClickEvent) {
            val itemId = accountPostings.getContainerDataSource.addItem(UUID.randomUUID())
            accountPostings.setValue(itemId)
            accountPostings.editingItemId = itemId
            accountPostings.editingPropertyId = AccountPropertyId.accountNr
            accountPostings.setEditable(true)
          }
        }
      )
    };

    lazy val removeTransactionRowButton = new Button() {
      setCaption("-")
      setEnabled(false)
      addClickListener(
        new ClickListener {
          def buttonClick(event: ClickEvent) = {
            accountPostings.getContainerDataSource.removeItem(accountPostings.getValue)
          }
        }
      )
    };

    lazy val balanceTransactionRowButton = new Button() {
      setCaption("Balance")
      setEnabled(false)
      addClickListener(
        new ClickListener {
          def buttonClick(event: ClickEvent) = {
            accountPostings.getContainerDataSource.addItem()
          }
        }
      )
    };

    lazy val other = new VerticalLayout() {
      setSizeFull()
      setSpacing(true)
      setMargin(true)

      addComponent(accountPostings)
      setComponentAlignment(accountPostings, Alignment.TOP_LEFT)
      setExpandRatio(accountPostings, 1.0f)
      val toolbar = new HorizontalLayout() {
        setSizeUndefined()
        setWidth("100%")

        addComponents(addTransactionRowButton, removeTransactionRowButton, balanceTransactionRowButton)
        setComponentAlignment(addTransactionRowButton, Alignment.MIDDLE_LEFT)
        setComponentAlignment(removeTransactionRowButton, Alignment.MIDDLE_LEFT)
        setComponentAlignment(balanceTransactionRowButton, Alignment.MIDDLE_RIGHT)
        setExpandRatio(balanceTransactionRowButton, 1.0f)
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


    object fieldFactory extends TableFieldFactory {

      val defaultFactory = DefaultFieldFactory.get()

      override def createField(container: Container, itemId: Any, propertyId: Any, uiContext: Component): Field[_] = {
        if (propertyId == AccountPropertyId.accountNr) {
          val select: ComboBox = new ComboBox() {
            setWidth("100%")
            setNullSelectionAllowed(false)
          }
          select.setContainerDataSource(presenter.getAccountsComboboxDatasource())
          select.setItemCaptionPropertyId(AccountPropertyId.accountNr)
          select.addValueChangeListener(new ValueChangeListener {
            def valueChange(event: ValueChangeEvent) {
              accountPostings.getContainerProperty(accountPostings.getValue, AccountPropertyId.accountDescription)
                .setValue(select.getContainerProperty(select.getValue, AccountPropertyId.accountDescription))
            }
          })
          if (accountPostings.get)
          return select
        } else {
          return defaultFactory.createField(container, itemId, propertyId, uiContext)
        }
      }

    }


    def asComponent() = rootLayout
  }


}
