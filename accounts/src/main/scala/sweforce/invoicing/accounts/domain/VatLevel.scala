package sweforce.invoicing.accounts.domain

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 9/19/12
 * Time: 7:22 AM
 * To change this template use File | Settings | File Templates.
 */

@SerialVersionUID(1l)
object VatLevel extends Enumeration with Serializable {
  type VatLevel = Value
  val Normal, Reduced, FurtherReduced = Value
}