package sweforce.invoicing.entries.create

import sweforce.gui.ap.place.Place
import sweforce.gui.ap.place.token.{PlaceTokenizer, Prefix}
import sweforce.vaadin.security.place.PlaceRequiresAuthentication

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 9/19/12
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Prefix("entries/new")
@PlaceRequiresAuthentication
class CreateEntryPlace extends Place{

  override def hashCode() = 7

  override def equals(p1: Any) = {
    (p1 != null && p1.isInstanceOf[CreateEntryPlace])
  }

}

object CreateEntryPlace {
  def apply() : CreateEntryPlace = new CreateEntryPlace
}

class CreateEntryPlaceTokenizer extends PlaceTokenizer[CreateEntryPlace] {

  def getPlace(p1: String) = CreateEntryPlace()

  def getToken(p1: CreateEntryPlace) = ""

}
