package sweforce.invoicing.accounts.infrastructure

import sweforce.state.StateEditor
import sweforce.invoicing.accounts.domain.{AccountType, VatLevel, AccountPropertyId, AccountId}


class AccountsEditor(val accountStoreId: Any, var state: Accounts) extends StateEditor[Accounts] {

  def this(accountStoreId: Any) = this(accountStoreId, new Accounts())

  def writeProperty(accountId: AccountId, propertyId: AccountPropertyId.Value, propertyValue: Any) = {
    this.applyChange(AccountPropertyChanged(accountStoreId, accountId, propertyId, propertyValue))
  }

  def delete(accountId: AccountId) = {
    this.applyChange(AccountDeleted(accountStoreId, accountId))
  }


  def add(accountId: AccountId, accountNr: String, accountDescription: String, vatLevel: VatLevel.Value, accountType: AccountType.Value,
          vatCode: String, taxCode: String) = {
    this.applyChange(AccountAdded(accountStoreId, accountId, accountNr, accountDescription, vatLevel, accountType, vatCode, taxCode))
  }
}
