package sweforce.accounting

import java.util.UUID

import akka.persistence.PersistentActor
import sweforce.accounting.Account.{AccountEvt, Debited, Debit}

class Account extends PersistentActor{

  override def receiveRecover: Receive = ???



  override def receiveCommand: Receive = {
    case Debit(id, transaction, amount) => {
      persist(Debited(id, transaction,  amount))
    }

  }

  override def persistenceId: String = ???

  def updateState(evt : AccountEvt): Unit = evt match {
    case Debited =>
  }
}

object Account {

  sealed trait AccountCmd

  case class Debit(account : UUID, transaction: UUID, amount: BigDecimal) extends AccountCmd
  case class Credit(account : UUID, transaction : UUID, amount: BigDecimal) extends AccountCmd

  sealed trait AccountEvt

  case class Debited(account : UUID, transaction: UUID,  amount: BigDecimal) extends AccountEvt
  case class Credited(account : UUID, transaction: UUID,  amount: BigDecimal) extends AccountEvt

}
