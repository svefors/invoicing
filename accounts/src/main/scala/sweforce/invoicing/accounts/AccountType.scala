package sweforce.invoicing.accounts

import java.io.Serializable

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/23/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */



object AccountType extends Enumeration with Serializable{
  type AccountType = Value
  val Asset, Debt, Equity, Income, Expense = Value

  override def equals(p1: Any) = {
    this == p1
  }
}


//sealed trait AccountType extends Serializable{
//
//
//}
//
//case object Asset extends AccountType{
//
//}
//
//case object Debt extends AccountType{
//
//}
//
//case object Equity extends AccountType{
//
//}
//
//case object Income extends AccountType{
//
//}
//
//case object Expense extends AccountType{
//
//}