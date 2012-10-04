package sweforce.invoicing.accounts.domain

import java.util.UUID


/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 8/20/12
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
object AccountType extends Enumeration with Serializable {
  type AccountType = Value
  val Asset, Debt, Equity, Income, Expense = Value

  override def equals(p1: Any) = {
    this == p1
  }
}






