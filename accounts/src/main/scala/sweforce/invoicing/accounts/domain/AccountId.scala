package sweforce.invoicing.accounts.domain

import java.util.UUID

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 9/14/12
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
case class AccountId(val id: String) {

  def this(uuid: UUID) = this(uuid.toString)

  def this() = this(UUID.randomUUID())

}

object AccountId {
  def apply() = {
    new AccountId()
  }
}