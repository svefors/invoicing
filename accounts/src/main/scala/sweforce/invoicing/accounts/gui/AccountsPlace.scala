package sweforce.invoicing.accounts.gui

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import sweforce.gui.ap.place.Place

//import vaadin.scala._

import sweforce.gui.ap.place.token.{PlaceTokenizer, Prefix}


import com.vaadin.data.{Item, Container}

import java.util.{Locale, UUID}
import sweforce.vaadin.{ComboBoxOverloaded}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.ui._
import sweforce.vaadin.security.place.PlaceRequiresAuthentication
import com.vaadin.event.MouseEvents.{ClickEvent, ClickListener}

import com.vaadin.ui.Component
import com.vaadin.ui.Field
import com.vaadin.ui.DefaultFieldFactory
import com.vaadin.ui

//import com.vaadin.ui._

/**
 * The chart of account can have
 */

@Prefix("accounts")
@PlaceRequiresAuthentication
class AccountsPlace extends Place {

  override def hashCode() = 13

  override def equals(p1: Any) = {
    (p1 != null && p1.isInstanceOf[AccountsPlace])
  }

}

object AccountsPlace {
  def apply(): AccountsPlace = new AccountsPlace
}

class AccountsPlaceTokenizer extends PlaceTokenizer[AccountsPlace] {

  def getPlace(p1: String) = new AccountsPlace

  def getToken(p1: AccountsPlace) = ""
}












