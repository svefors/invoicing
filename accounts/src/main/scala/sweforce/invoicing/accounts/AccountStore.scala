package sweforce.invoicing.accounts


import java.util.UUID


/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 9/14/12
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */

object AccountPropertyId extends Enumeration {
  type AccountPropertyId = Value
  val accountId, accountNr, accountDescription, accountType, vatLevel, vatCode, taxCode = Value
}

trait PropertyValueGet {

  def getPropertyValue[T](propertyId: AccountPropertyId.Value): Option[T]

}

trait AccountPropertyValueGet {

  def get[T](accountId: AccountId, propertyId: AccountPropertyId.Value): Option[T]

}

case class AccountStore(val accountingSystemIdentifier: UUID, accountMap: Map[AccountPropertyId.Value, Map[AccountId, Any]])
  extends AccountPropertyValueGet {

  def get[T](accountId: AccountId, propertyId: AccountPropertyId.Value): Option[T] = {
    accountMap.get(propertyId) match {
      case Some(propertyMap) => {
        propertyMap.get(accountId) match {
          case Some(value) => {
            return Some(value.asInstanceOf[T])
          }
        }
      }
    }
  }

  def accountIds() = {
    accountMap(AccountPropertyId.accountNr).keys
  }

  def set(accountId: AccountId, propertyId: AccountPropertyId.Value, propertyValue: Any): AccountStore = {
    if (propertyValue == null)
      set(accountId, propertyId, None)
    else
      set(accountId, propertyId, Some(propertyValue))
  }

  def set(accountId: AccountId, propertyId: AccountPropertyId.Value, propertyValue: Option[Any]): AccountStore = {
    var propertyMap = (accountMap.get(propertyId) match {
      case Some(propertyMap) => {
        propertyMap
      }
      case None => {
        Map[AccountId, Any]()
      }
    })
    propertyValue match {
      case Some(value) => {
        propertyMap = propertyMap + (accountId -> value)
      }
      case None => {
        propertyMap = propertyMap - accountId
      }
    }
    copy(accountMap = accountMap + (propertyId -> propertyMap))
  }

  def delete(accountId: AccountId): AccountStore = {
    var store = copy()
    AccountPropertyId.values.foreach(propertyId => {
      store = store.set(accountId, propertyId, None)
    })
    store
  }

  def handle(command: AccountStoreCommand): AccountStore = {
    command match {
      case DeleteAccount(accountId) => delete(accountId)
      case AccountPropertyCommand(accountId, propertyId, value) => set(accountId, propertyId, Some(value))
    }
  }

  def add(accountId: AccountId, accountNr: String, accountDescription: String, vatLevel: VatLevel.Value, accountType: AccountType.Value,
          vatCode: String, taxCode: String) = {
    set(accountId, AccountPropertyId.accountNr, accountNr)
      .set(accountId, AccountPropertyId.accountDescription, accountDescription)
      .set(accountId, AccountPropertyId.vatLevel, vatLevel)
      .set(accountId, AccountPropertyId.accountType, accountType)
      .set(accountId, AccountPropertyId.vatCode, vatCode)
      .set(accountId, AccountPropertyId.taxCode, taxCode)
  }
}


case class DeleteAccount(val accountId: AccountId) extends AccountStoreCommand {}

case class AccountPropertyCommand(val accountId: AccountId, val propertyIdentifier: AccountPropertyId.Value, val value: Any)
  extends AccountStoreCommand with AccountPropertyIdCommand {}

sealed trait AccountStoreCommand {
  val accountId: AccountId
}

sealed trait AccountPropertyIdCommand {
  val propertyIdentifier: AccountPropertyId.Value
}


object AccountStore {

  def handle(command: AccountStoreCommand)= {
    command match {
      case DeleteAccount(accountId) => delete(accountId)
      case AccountPropertyCommand(accountId, propertyId, value) => set(accountId, propertyId, Some(value))
    }
  }

  lazy val _baskontoplan = AccountStore(UUID.randomUUID(), Map())
    .add(AccountId(), "1100", "Byggnader", null, AccountType.Asset, "", "B2")
    .add(AccountId(), "1130", "Mark", null, AccountType.Asset, "", "B3")
    .add(AccountId(), "1910", "Kassa", null, AccountType.Asset, "", "B9")
    .add(AccountId(), "1920", "Bankkonto", null, AccountType.Asset, "", "B9")
    .add(AccountId(), "2010", "Eget Kapital", null, AccountType.Debt, "", "B10")
    .add(AccountId(), "2019", "Årets Resultat", null, AccountType.Debt, "", "B10")
    .add(AccountId(), "2610", "Utgaende moms, oreducerad", null, AccountType.Debt, "10", "B14")
    .add(AccountId(), "2620", "Utgaende moms, reducerad 1", null, AccountType.Debt, "11", "B14")
    .add(AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14")
    .add(AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14")
    .add(AccountId(), "2640", "Ingaende moms", null, AccountType.Debt, "48", "B14")
    .add(AccountId(), "2650", "Moms redovisningskonto", null, AccountType.Debt, "49", "B14")
    .add(AccountId(), "3010", "Forsaljning", VatLevel.Normal, AccountType.Income, "05", "R1")
    .add(AccountId(), "3041", "Forsaljning inom sverige", VatLevel.Normal, AccountType.Income, "05", "R1")
    .add(AccountId(), "3051", "Forsaljning inom sverige, bocker", VatLevel.Reduced, AccountType.Income, "11", "R1")
    .add(AccountId(), "3102", "Forsaljning utomlands (EU)", null, AccountType.Income, "35", "R1")
    .add(AccountId(), "5400", "Forbrukningsinventarier och forbrukningsmaterial", null, AccountType.Expense, "", "R6")
    .add(AccountId(), "5800", "Resekostnader", null, AccountType.Expense, "", "R6")
    .add(AccountId(), "5891", "Skattefria traktamente - utlandet", null, AccountType.Expense, "", "R6")
    .add(AccountId(), "8330", "Valutakursforluster pa fodringar", null, AccountType.Expense, "", "R4")
    .add(AccountId(), "8990", "Resultat", null, AccountType.Expense, "", "R11")

  def baskontoplan() = {
    _baskontoplan
    /*
         AccountEntry("0010", "Freehold property", AccountType.Asset),
     AccountEntry("0020", "Plant and machiner", AccountType.Asset),
     AccountEntry("0021", "Plant and machinery depreciation", AccountType.Asset),
     AccountEntry("0030", "Office equipment", AccountType.Asset),
     AccountEntry("0031", "Office equipment depreciation", AccountType.Asset),
     AccountEntry("1200", "Bank current account", AccountType.Asset),
     AccountEntry("1210", "Bank deposit account", AccountType.Asset),
     AccountEntry("2200", "Sales VAT 20%", AccountType.Debt),
     AccountEntry("2201", "Purchase VAT", AccountType.Debt),
     AccountEntry("2205", "Sales VAT 5%", AccountType.Debt),
     AccountEntry("3200", "Profit and loss", AccountType.Equity),
     AccountEntry("4000", "Sales", AccountType.Income),
     AccountEntry("4010", "EU export sales", AccountType.Income),
     AccountEntry("4020", "Outside EU export sales", AccountType.Income),
     AccountEntry("4900", "Misc income", AccountType.Income),
     AccountEntry("4902", "Foreign exchange gain", AccountType.Income),
     AccountEntry("5000", "Materials purchased", AccountType.Expense),
     AccountEntry("7500", "Postage and carriage", AccountType.Expense),
     AccountEntry("7501", "Office stationery", AccountType.Expense),
     AccountEntry("7502", "Telephone, internet", AccountType.Expense),
     AccountEntry("7710", "Computer Expenses", AccountType.Expense)
    */
  }
}


trait Versioned {
  val version: Int;

}




