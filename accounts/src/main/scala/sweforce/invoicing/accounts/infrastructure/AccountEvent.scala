package sweforce.invoicing.accounts.infrastructure

import sweforce.invoicing.accounts.domain.{AccountPropertyId, AccountType, VatLevel, AccountId}


/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
sealed trait AccountEvent extends Serializable {
  val accountStoreId: Any
  val accountId: AccountId
}

@SerialVersionUID(1)
case class AccountDeleted(val accountStoreId: Any, val accountId: AccountId) extends AccountEvent {}

@SerialVersionUID(1)
case class AccountAdded(val accountStoreId: Any, val accountId: AccountId, val accountNr: String,
                      val accountDescription: String, val vatLevel: VatLevel.Value, val accountType: AccountType.Value,
                      val vatCode: String, val taxCode: String) extends AccountEvent {}

@SerialVersionUID(1)
case class AccountPropertyChanged(val accountStoreId: Any, val accountId: AccountId, val propertyIdentifier: AccountPropertyId.Value, val value: Any)
  extends AccountEvent with AccountPropertyIdCommand {}

sealed trait AccountPropertyIdCommand {
  val propertyIdentifier: AccountPropertyId.Value

}
