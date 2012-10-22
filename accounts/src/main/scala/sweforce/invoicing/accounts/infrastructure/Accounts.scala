package sweforce.invoicing.accounts.infrastructure

import sweforce.state.State
import sweforce.invoicing.accounts.domain.{AccountType, VatLevel, AccountPropertyId, AccountId}
import scala.Some
import java.util.UUID

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
case class Accounts(val accountStoreId: Any, val accountIdSet : Set[AccountId], val accountMap: Map[AccountPropertyId.Value, Map[AccountId, Any]]) extends State[Accounts] {

  def this(accountStoreId: Any) = this(accountStoreId, Set[AccountId](), Map[AccountPropertyId.Value, Map[AccountId, Any]]())

  def this() = this(UUID.randomUUID())

  def applyChange(change: Any): Accounts = {
    change.asInstanceOf[AccountEvent] match {
      case AccountDeleted(accountStoreId, accountId) => {
        return delete(accountId)
      }
      case AccountPropertyChanged(accountStoreId, accountId, propertyId, propertyValue) => {
        return writeProperty(accountId, propertyId, propertyValue)
      }
      case AccountAdded(accountStoreId, accountId, accountNr, accountDescription, vatLevel, accountType, vatCode, taxCode) => {
        return add(accountId, accountNr, accountDescription, vatLevel, accountType, vatCode, taxCode)
      }
    }
  }

  private def delete(accountId: AccountId) = {
    copy(accountIdSet = accountIdSet - accountId)
      .writeProperty(accountId, AccountPropertyId.accountNr, null)
      .writeProperty(accountId, AccountPropertyId.accountDescription, null)
      .writeProperty(accountId, AccountPropertyId.vatLevel, null)
      .writeProperty(accountId, AccountPropertyId.accountType, null)
      .writeProperty(accountId, AccountPropertyId.vatCode, null)
      .writeProperty(accountId, AccountPropertyId.taxCode, null)

  }

  private def add(accountId: AccountId, accountNr: String, accountDescription: String, vatLevel: VatLevel.Value, accountType: AccountType.Value,
                  vatCode: String, taxCode: String): Accounts = {
    copy(accountIdSet = accountIdSet + accountId)
      .writeProperty(accountId, AccountPropertyId.accountNr, accountNr)
      .writeProperty(accountId, AccountPropertyId.accountDescription, accountDescription)
      .writeProperty(accountId, AccountPropertyId.vatLevel, vatLevel)
      .writeProperty(accountId, AccountPropertyId.accountType, accountType)
      .writeProperty(accountId, AccountPropertyId.vatCode, vatCode)
      .writeProperty(accountId, AccountPropertyId.taxCode, taxCode)
  }

  private def writeProperty(accountId: AccountId, propertyId: AccountPropertyId.Value, propertyValue: Any): Accounts = {
    var accountIdMap = accountMap.get(AccountPropertyId.accountId) match {
      case Some(accountIdMap) => accountIdMap
      case None => Map[AccountId, Any]()
    }

    var propertyMap = (accountMap.get(propertyId) match {
      case Some(propertyMap) => propertyMap
      case None => Map[AccountId, Any]()
    })

    if (propertyValue == null){
      propertyMap = propertyMap - accountId
      accountIdMap = accountIdMap - accountId
    }else{
      propertyMap = propertyMap + (accountId -> propertyValue)
      accountIdMap = accountIdMap + (accountId -> accountId)
    }
    copy(accountMap = accountMap + (propertyId -> propertyMap) + (AccountPropertyId.accountId -> accountIdMap))
  }

  def accountIds() = {
    accountIdSet
//    accountMap.get(AccountPropertyId.accountId) match {
//      case Some(map) => map.keySet
//      case None => Set[AccountId]()
//    }
  }

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
}
