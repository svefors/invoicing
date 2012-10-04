package sweforce.invoicing.accounts.domain

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1)
object AccountPropertyId extends Enumeration with Serializable{
  type AccountPropertyId = Value
  val accountId, accountNr, accountDescription, accountType, vatLevel, vatCode, taxCode = Value
}
