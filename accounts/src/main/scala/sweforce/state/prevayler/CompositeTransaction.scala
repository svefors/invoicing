package sweforce.state.prevayler

import org.prevayler.Transaction
import java.util.Date

@SerialVersionUID(1l)
case class CompositeTransaction(val stateTransactions: Seq[Transaction]) extends Transaction{

  /**
   *
   * @param prevalentSystem
   * @param executionTime
   */
  def executeOn(prevalentSystem: Any, executionTime: Date) {
    stateTransactions.foreach(st => st.executeOn(prevalentSystem, executionTime))
  }



}
