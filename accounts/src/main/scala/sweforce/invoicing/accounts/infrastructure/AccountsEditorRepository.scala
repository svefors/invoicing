package sweforce.invoicing.accounts.infrastructure

import sweforce.state.prevayler.{TransactionFactory, PrevaylerStateEditorRepository}
import sweforce.state.{StateRepository, State, StateEditorFactory, StateEditor}
import sweforce.invoicing.prevalence.RootStore

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
object AccountsEditorRepository extends PrevaylerStateEditorRepository[Accounts, AccountsEditor] with TransactionFactory {

  val stateEditorFactory = new StateEditorFactory[Accounts, AccountsEditor] {
    def createStateEditor(state: Accounts) = new AccountsEditor(state.accountStoreId, state)
  }

  val stateRepository = new StateRepository[Accounts] {
    def get(identifier: Any) = RootStore.getInstance().accountSettings.get(identifier) match{
      case Some(x) => x
      case None => new Accounts(identifier)
    }

//    def put(identifier: Any, state: Accounts) {}
  }


  //  def createTransaction(stateEditor: AccountsEditor) = {
  //
  //  }


  //  def createTransaction(stateEditor: StateEditor[_]) = {
  //    AccountsTransaction(stateEditor.asInstanceOf[AccountsEditor].accountStoreId, stateEditor.changes.map(any => any.asInstanceOf[AccountEvent]))
  //  }
  //
  //  def createTransaction[S <: State[S]](stateEditor: StateEditor[S]) = {
  //    AccountsTransaction(stateEditor.asInstanceOf[AccountsEditor].accountStoreId, stateEditor.changes.map(any => any.asInstanceOf[AccountEvent]))
  //  }


  def createTransaction(stateEditor: StateEditor[_]) = {
    AccountsTransaction(stateEditor.asInstanceOf[AccountsEditor].accountStoreId, stateEditor.changes.map(any => any.asInstanceOf[AccountEvent]))
  }



}


