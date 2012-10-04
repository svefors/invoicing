package sweforce.state

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
trait StateEditorFactory[S <: State[S], SE <: StateEditor[S]] {

  def createStateEditor(state : S) : SE

}

