package sweforce.accounting

import java.util.UUID

import akka.actor.{ActorPath, ActorRef, Actor}
import akka.persistence.{AtLeastOnceDelivery, PersistentActor}
import sweforce.accounting.Account.{Credited, Debited}
import sweforce.accounting.Journal.{TransactionPosted, PostTransaction}

class Journal(id : UUID, val accounts : ActorPath) extends PersistentActor with AtLeastOnceDelivery{

  override def receiveRecover: Receive = {
    case tp : TransactionPosted => updateState(tp)
  }

  override def receiveCommand: Receive = {
    case PostTransaction(transaction) => {
      //validate transaction
      persist(TransactionPosted(transaction))(updateState)
    }
    case Debited => {
      //confirm delivery
    }
    case Credited=> {

    }
  }

  def updateState(transactionPosted : TransactionPosted): Unit = {
    transactionPosted.transaction.entries.foreach(accountCmd => deliver(accounts,  _ => accountCmd))
  }

  override def persistenceId: String = id.toString
}

object Journal {
  case class PostTransaction(transaction : Transaction)
  case class TransactionPosted(transaction : Transaction)
}



