package sweforce.invoicing.accounts.infrastructure

import sweforce.state.StateRepository
import sweforce.invoicing.prevalence.RootStore

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
class AccountsRepository(val rootStore : RootStore) extends StateRepository[Accounts] with Serializable{
  def get(identifier: Any) = rootStore.accountSettings(identifier)

  def put(identifier: Any, state: Accounts) {
    rootStore.accountSettings = rootStore.accountSettings + (identifier -> state)
  }
}
