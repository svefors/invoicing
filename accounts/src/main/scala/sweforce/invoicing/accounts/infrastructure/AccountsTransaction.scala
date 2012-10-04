package sweforce.invoicing.accounts.infrastructure


import sweforce.invoicing.prevalence.RootStore
import org.prevayler.Transaction
import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
case class AccountsTransaction(val accountsStoreId: Any, val events: List[AccountEvent])
  extends Transaction {

  def executeOn(prevalentSystem: Any, executionTime: Date) {
    val rootstore = prevalentSystem.asInstanceOf[RootStore]
    val currentState = rootstore.accountSettings.get(accountsStoreId) match {
      case Some(accounts) => accounts
      case None => new Accounts(accountsStoreId)
    }
    val newState = currentState.applyChanges(events)
    rootstore.accountSettings = rootstore.accountSettings + (newState.accountStoreId -> newState)
  }
}
