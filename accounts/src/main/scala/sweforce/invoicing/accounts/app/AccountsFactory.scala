package sweforce.invoicing.accounts.app


import sweforce.invoicing.accounts.domain.{VatLevel, AccountType, AccountId}

import sweforce.invoicing.accounts.infrastructure.AccountsEditorRepository
import sweforce.invoicing.prevalence.RootStoreUnitOfWork

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 9/24/12
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
object AccountsFactory {

  val accountStoreId = "DEMO"


  def ensureDemoDataIsCreated() = {
    var accountsEditor = AccountsEditorRepository.load(accountStoreId)
    if (accountsEditor.state.accountIds().size == 0) {
      val uow = new RootStoreUnitOfWork
      uow.registerStateEditor(accountsEditor, AccountsEditorRepository)
      accountsEditor.add(AccountId(), "1100", "Byggnader", null, AccountType.Asset, "", "B2")
      accountsEditor.add(AccountId(), "1130", "Mark", null, AccountType.Asset, "", "B3")
      accountsEditor.add(AccountId(), "1910", "Kassa", null, AccountType.Asset, "", "B9")
      accountsEditor.add(AccountId(), "1920", "Bankkonto", null, AccountType.Asset, "", "B9")
      accountsEditor.add(AccountId(), "2010", "Eget Kapital", null, AccountType.Debt, "", "B10")
      accountsEditor.add(AccountId(), "2019", "Årets Resultat", null, AccountType.Debt, "", "B10")
      accountsEditor.add(AccountId(), "2610", "Utgaende moms, oreducerad", null, AccountType.Debt, "10", "B14")
      accountsEditor.add(AccountId(), "2620", "Utgaende moms, reducerad 1", null, AccountType.Debt, "11", "B14")
      accountsEditor.add(AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14")
      //    AddAccount(accountStoreId, AccountId(), "2630", "Utgaende moms, reducerad 2", null, AccountType.Debt, "12", "B14"),
      accountsEditor.add(AccountId(), "2640", "Ingaende moms", null, AccountType.Debt, "48", "B14")
      accountsEditor.add(AccountId(), "2650", "Moms redovisningskonto", null, AccountType.Debt, "49", "B14")
      accountsEditor.add(AccountId(), "3010", "Forsaljning", VatLevel.Normal, AccountType.Income, "05", "R1")
      accountsEditor.add(AccountId(), "3041", "Forsaljning inom sverige", VatLevel.Normal, AccountType.Income, "05", "R1")
      accountsEditor.add(AccountId(), "3051", "Forsaljning inom sverige, bocker", VatLevel.Reduced, AccountType.Income, "11", "R1")
      accountsEditor.add(AccountId(), "3102", "Forsaljning utomlands (EU)", null, AccountType.Income, "35", "R1")
      accountsEditor.add(AccountId(), "5400", "Forbrukningsinventarier och forbrukningsmaterial", null, AccountType.Expense, "", "R6")
      accountsEditor.add(AccountId(), "5800", "Resekostnader", null, AccountType.Expense, "", "R6")
      accountsEditor.add(AccountId(), "5891", "Skattefria traktamente - utlandet", null, AccountType.Expense, "", "R6")
      accountsEditor.add(AccountId(), "8330", "Valutakursforluster pa fodringar", null, AccountType.Expense, "", "R4")
      accountsEditor.add(AccountId(), "8990", "Resultat", null, AccountType.Expense, "", "R11")
      uow.commit()
    }

  }


}
