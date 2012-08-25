package sweforce.invoicing.accounts

import java.util.UUID
import currency.Currency
import reflect.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 8/20/12
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 */

class AccountSettings(val id: UUID) {

  def findAccounts() : Seq[AccountSettingRow] ={
    AccountEntry.getUkChartOfAccounts()
  }


}

/**
 * Alt1. some form of implicit methods to convert from stored representation to this representation?
 * i.e. a BeanItem
 *
 * Alt2. why not use the
 *
 */
class AccountSettingRow {

  @BeanProperty
  var accountId : UUID

  @BeanProperty
  var accountnr : String

  @BeanProperty
  var accountName : String

  @BeanProperty
  var accountType : AccountType.AccountType

  @BeanProperty
  var vatReportCode : String

  @BeanProperty
  var taxReportCode : String

  @BeanProperty
  val currency : Currency


}








