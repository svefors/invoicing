package sweforce.invoicing.accounts.infrastructure


import sweforce.invoicing.accounts.domain._
import scala.Some
import java.util.UUID

import sweforce.invoicing.accounts.domain.AccountPropertyId.Val
/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 9/21/12
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
class InMemoryAccountStore(val accountStoreId: Any, var accountMap: Map[AccountPropertyId.Value, Map[AccountId, Any]])
  extends Serializable {

  def this(accountStoreId: Any) = this(accountStoreId, Map[AccountPropertyId.Value, Map[AccountId, Any]]())

  def this() = this(UUID.randomUUID())

  def readProperty[T](accountId: AccountId, propertyId: AccountPropertyId.Value): T = {
    accountMap.get(propertyId) match {
      case Some(propertyMap) => {
        propertyMap.get(accountId) match {
          case Some(value) => {
            return value.asInstanceOf[T]
          }
          case None => return null.asInstanceOf[T]
        }
      }
      case None => return null.asInstanceOf[T]
    }
  }

  def accountIds() = {
    accountMap.get(AccountPropertyId.accountNr) match {
      case Some(map) => map.keySet
      case None => Set[AccountId]()
    }
  }


  def readPropertyValues(propertyId: AccountPropertyId.Value) = {
    accountMap.get(propertyId) match {
      case Some(propertyMap) => {
        propertyMap
      }
      case None => {
        null
      }
    }
  }

  def writeProperty(accountId: AccountId, propertyId: AccountPropertyId.Value, propertyValue: Any) = {
    var propertyMap = (accountMap.get(propertyId) match {
      case Some(propertyMap) => {
        propertyMap
      }
      case None => {
        Map[AccountId, Any]()
      }
    })
    if (propertyValue == null)
      propertyMap = propertyMap - accountId
    else
      propertyMap = propertyMap + (accountId -> propertyValue)
    accountMap = accountMap + (propertyId -> propertyMap)
  }



  def delete(accountId: AccountId) = {
    //    var store = copy()
    AccountPropertyId.values.foreach(propertyId => {
      this.writeProperty(accountId, propertyId, None)
    })

  }


  def add(accountId: AccountId, accountNr: String, accountDescription: String, vatLevel: VatLevel.Value, accountType: AccountType.Value,
          vatCode: String, taxCode: String) = {
    writeProperty(accountId, AccountPropertyId.accountNr, accountNr)
    writeProperty(accountId, AccountPropertyId.accountDescription, accountDescription)
    writeProperty(accountId, AccountPropertyId.vatLevel, vatLevel)
    writeProperty(accountId, AccountPropertyId.accountType, accountType)
    writeProperty(accountId, AccountPropertyId.vatCode, vatCode)
    writeProperty(accountId, AccountPropertyId.taxCode, taxCode)
  }

  //  add(AccountId(), "1100", "Byggnader", null, AccountType.Asset, "", "B2")
  //  add(AccountId(), "1130", "Mark", null, AccountType.Asset, "", "B3")
  //  add(AccountId(), "1910", "Kassa", null, AccountType.Asset, "", "B9")
  //  add(AccountId(), "1920", "Bankkonto", null, AccountType.Asset, "", "B9")
  //  add(AccountId(), "2010", "Eget Kapital", null, AccountType.Debt, "", "B10")
  //  add(AccountId(), "2019", "Årets Resultat", null, AccountType.Debt, "", "B10")
  //  add(AccountId(), "2610", "Utgaende moms, oreducerad", null, AccountType.Debt, "10", "B14")
  //  add(AccountId(), "2620", "Utgaende moms, reducerad 1", null, AccountType.Debt, "11", "B14")
  //  add(AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14")
  //  add(AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14")
  //  add(AccountId(), "2640", "Ingaende moms", null, AccountType.Debt, "48", "B14")
  //  add(AccountId(), "2650", "Moms redovisningskonto", null, AccountType.Debt, "49", "B14")
  //  add(AccountId(), "3010", "Forsaljning", VatLevel.Normal, AccountType.Income, "05", "R1")
  //  add(AccountId(), "3041", "Forsaljning inom sverige", VatLevel.Normal, AccountType.Income, "05", "R1")
  //  add(AccountId(), "3051", "Forsaljning inom sverige, bocker", VatLevel.Reduced, AccountType.Income, "11", "R1")
  //  add(AccountId(), "3102", "Forsaljning utomlands (EU)", null, AccountType.Income, "35", "R1")
  //  add(AccountId(), "5400", "Forbrukningsinventarier och forbrukningsmaterial", null, AccountType.Expense, "", "R6")
  //  add(AccountId(), "5800", "Resekostnader", null, AccountType.Expense, "", "R6")
  //  add(AccountId(), "5891", "Skattefria traktamente - utlandet", null, AccountType.Expense, "", "R6")
  //  add(AccountId(), "8330", "Valutakursforluster pa fodringar", null, AccountType.Expense, "", "R4")
  //  add(AccountId(), "8990", "Resultat", null, AccountType.Expense, "", "R11")

}


//object InMemoryAccountStore {
//
////  def apply(command: AccountStoreCommand)= {
////    command match {
////      case DeleteAccount(accountId) => delete(accountId)
////      case AccountPropertyCommand(accountId, propertyId, value) => set(accountId, propertyId, Some(value))
////    }
////  }
//
//  lazy val _baskontoplan = new InMemoryAccountStore(UUID.randomUUID(), Map()){
//    add(AccountId(), "1100", "Byggnader", null, AccountType.Asset, "", "B2")
//    add(AccountId(), "1130", "Mark", null, AccountType.Asset, "", "B3")
//    add(AccountId(), "1910", "Kassa", null, AccountType.Asset, "", "B9")
//    add(AccountId(), "1920", "Bankkonto", null, AccountType.Asset, "", "B9")
//    add(AccountId(), "2010", "Eget Kapital", null, AccountType.Debt, "", "B10")
//    add(AccountId(), "2019", "Årets Resultat", null, AccountType.Debt, "", "B10")
//    add(AccountId(), "2610", "Utgaende moms, oreducerad", null, AccountType.Debt, "10", "B14")
//    add(AccountId(), "2620", "Utgaende moms, reducerad 1", null, AccountType.Debt, "11", "B14")
//    add(AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14")
//    add(AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14")
//    add(AccountId(), "2640", "Ingaende moms", null, AccountType.Debt, "48", "B14")
//    add(AccountId(), "2650", "Moms redovisningskonto", null, AccountType.Debt, "49", "B14")
//    add(AccountId(), "3010", "Forsaljning", VatLevel.Normal, AccountType.Income, "05", "R1")
//    add(AccountId(), "3041", "Forsaljning inom sverige", VatLevel.Normal, AccountType.Income, "05", "R1")
//    add(AccountId(), "3051", "Forsaljning inom sverige, bocker", VatLevel.Reduced, AccountType.Income, "11", "R1")
//    add(AccountId(), "3102", "Forsaljning utomlands (EU)", null, AccountType.Income, "35", "R1")
//    add(AccountId(), "5400", "Forbrukningsinventarier och forbrukningsmaterial", null, AccountType.Expense, "", "R6")
//    add(AccountId(), "5800", "Resekostnader", null, AccountType.Expense, "", "R6")
//    add(AccountId(), "5891", "Skattefria traktamente - utlandet", null, AccountType.Expense, "", "R6")
//    add(AccountId(), "8330", "Valutakursforluster pa fodringar", null, AccountType.Expense, "", "R4")
//    add(AccountId(), "8990", "Resultat", null, AccountType.Expense, "", "R11")
//  }
//
//  def baskontoplan() = {
//    _baskontoplan
//    /*
//         AccountEntry("0010", "Freehold property", AccountType.Asset),
//     AccountEntry("0020", "Plant and machiner", AccountType.Asset),
//     AccountEntry("0021", "Plant and machinery depreciation", AccountType.Asset),
//     AccountEntry("0030", "Office equipment", AccountType.Asset),
//     AccountEntry("0031", "Office equipment depreciation", AccountType.Asset),
//     AccountEntry("1200", "Bank current account", AccountType.Asset),
//     AccountEntry("1210", "Bank deposit account", AccountType.Asset),
//     AccountEntry("2200", "Sales VAT 20%", AccountType.Debt),
//     AccountEntry("2201", "Purchase VAT", AccountType.Debt),
//     AccountEntry("2205", "Sales VAT 5%", AccountType.Debt),
//     AccountEntry("3200", "Profit and loss", AccountType.Equity),
//     AccountEntry("4000", "Sales", AccountType.Income),
//     AccountEntry("4010", "EU export sales", AccountType.Income),
//     AccountEntry("4020", "Outside EU export sales", AccountType.Income),
//     AccountEntry("4900", "Misc income", AccountType.Income),
//     AccountEntry("4902", "Foreign exchange gain", AccountType.Income),
//     AccountEntry("5000", "Materials purchased", AccountType.Expense),
//     AccountEntry("7500", "Postage and carriage", AccountType.Expense),
//     AccountEntry("7501", "Office stationery", AccountType.Expense),
//     AccountEntry("7502", "Telephone, internet", AccountType.Expense),
//     AccountEntry("7710", "Computer Expenses", AccountType.Expense)
//    */
//  }
//}
