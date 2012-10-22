package sweforce.invoicing.accounts.gui

import com.vaadin.data.{Container, Property}
import java.beans.{PropertyVetoException, PropertyChangeListener}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener, ValueChangeNotifier}
import sweforce.command.Command
import com.vaadin.data.Container.ItemSetChangeNotifier
import sweforce.vaadin.scala.VaadinView
import com.vaadin.ui._
import sweforce.vaadin.table.EditableTable
import sweforce.invoicing.accounts.domain.{VatLevel, AccountType, AccountId, AccountPropertyId}
import scala.Array
import com.vaadin.ui.Button.{ClickEvent, ClickListener}
import com.vaadin.ui.Alignment
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Table
import sweforce.vaadin.command.button.CommandButton

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/16/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
class AccountSettingsView extends VaadinView {

  def setViewModel(viewModel: ViewModel) {
    addAccountButton.setCommand(viewModel.getAddCommand)
    removeAccountButton.setCommand(viewModel.getRemoveCommand)
    accountOverviewTable.setContainerDataSource(viewModel.getAccountsContainer)
  }

  lazy val addAccountButton = new CommandButton() {
    setCaption("+")
  };

  lazy val removeAccountButton = new CommandButton() {
    setCaption("-")
  };
  lazy val searchFilter = new TextField() {
    setInputPrompt("Search")
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

  lazy val rootContainer = new VerticalLayout() {
    setSizeFull()
    addComponents(accountOverviewTable, bottomToolbar)
    setComponentAlignment(accountOverviewTable, Alignment.TOP_LEFT)
    setComponentAlignment(bottomToolbar, Alignment.BOTTOM_LEFT)
    setExpandRatio(accountOverviewTable, 1.0f)
  }

  lazy val accountOverviewTable: Table with EditableTable = new Table() with EditableTable {
    setSizeFull()
    setSelectable(true)
    setImmediate(true)
    //    setContainerDataSource(presenter.accountsContainer)
    getVisibleColumns(
      List(AccountPropertyId.accountNr, AccountPropertyId.accountDescription,
        AccountPropertyId.vatLevel,
        AccountPropertyId.accountType,
        AccountPropertyId.vatCode,
        AccountPropertyId.taxCode,
        "balance").toArray
    )
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

  def asComponent() = rootContainer

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

  trait ViewModel {

    val getAddCommand: Command

    val getRemoveCommand: Command

    val getSearchCommand: Command

    /**
     * must have the correct container properties
     */
    val getAccountsContainer: Container with ItemSetChangeNotifier with Container.Sortable

  }

}
