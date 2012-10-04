package sweforce.state

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 8:02 AM
 * To change this template use File | Settings | File Templates.
 */
trait StateEditorRepository[S <: State[S], SE <: StateEditor[S]] {

  val stateEditorFactory: StateEditorFactory[S, SE]

  val stateRepository: StateRepository[S]

  def load(identifier: Any): SE = {
    val stateEditor: SE = stateEditorFactory.createStateEditor(stateRepository.get(identifier))
    stateEditor
  }

//  protected def createSaveCallback(se : SE) : SaveCallback[SE]


}
