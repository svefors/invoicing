package sweforce.state.prevayler

import sweforce.state.{StateEditorRepository, StateEditor, State}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
trait PrevaylerStateEditorRepository[S <: State[S], SE <: StateEditor[S]] extends StateEditorRepository[S, SE] {

//  def createTransaction(stateEditor : SE) : StateRepositoryTransaction[S]


}
