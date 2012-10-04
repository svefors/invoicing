package sweforce.state.prevayler

import sweforce.state.StateEditor
import org.prevayler.Transaction

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
trait TransactionFactory {

  def createTransaction(stateEditor : StateEditor[_]) : Transaction

}
