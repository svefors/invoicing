package sweforce.state.prevayler

import sweforce.state.{State, StateEditor, UnitOfWork}
import org.prevayler.{Transaction, Prevayler}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
class PrevaylerUnitOfWork(val prevayler: Prevayler) extends UnitOfWork {

  var editors = Set[StateEditor[_]]()

  var transactionFactories = Map[StateEditor[_], TransactionFactory]()

  def registerStateEditor[S <: State[S]](editor: StateEditor[S], transactionFactory: TransactionFactory) {
    editors = editors + editor
    transactionFactories = transactionFactories + (editor -> transactionFactory)
  }

  override def commit() {
    var transactions = List[Transaction]()
    editors.foreach(st => {
      transactions = transactions ++ List(transactionFactories(st).createTransaction(st))
    })
    prevayler.execute(CompositeTransaction(transactions))
  }


  def rollback() {
    throw new UnsupportedOperationException()
  }
}
