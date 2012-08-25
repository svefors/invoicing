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












