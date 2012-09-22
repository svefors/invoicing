package sweforce.invoicing.accounts

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 9/19/12
 * Time: 7:22 AM
 * To change this template use File | Settings | File Templates.
 */

object VatLevel extends Enumeration{
  type VatLevel = Value
  val Normal, Reduced, FurtherReduced = Value
}