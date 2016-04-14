package sweforce.invoicing.accounts

import java.util.UUID

import akka.actor.Actor
import akka.actor.Actor.Receive
import sweforce.invoicing.accounts.domain.Account

sealed trait ChartOfAccountsCommand

case class AddAccount(val id : UUID, val account : Account) extends ChartOfAccountsCommand
case class EditAccount(val id : UUID, val account : Account) extends ChartOfAccountsCommand
case class DeleteAccount(val id : UUID) extends ChartOfAccountsCommand

class ChartOfAccounts extends Actor{

  var accounts = Map[UUID, Account]()

  var accountNrs : Set[String] = Set()

  def updateState(command : ChartOfAccountsCommand) {
    command match {
      case AddAccount(id, account) => {
        accounts += (id -> account)
        accountNrs += account.nr
      }
      case EditAccount(id, account) => {
        accounts += (id -> account)
        accountNrs += account.nr
      }
      case DeleteAccount(id) => {
        accountNrs -= accounts(id).nr
        accounts -= id
      }
    }
  }

  override def receive: Receive = {
    case cmd@AddAccount(id, account) => {
      (accounts.get(id), accountNrs.contains(account.nr)) match {
        case (None, false) => updateState(cmd)
      }
    }
    case cmd@EditAccount(id, account) => {
      (accounts.get(id), accountNrs.contains(account.nr)) match {
        case (Some(_), false) => updateState(cmd)
      }
    }
    case cmd@DeleteAccount(id) => {
      // can only delete accounts that don't have transactions posted to them?
      accounts.get(id) match {
        case Some(account)=> updateState(cmd)
      }
    }
  }
}

object ChartOfAccounts {

}

