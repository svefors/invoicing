package sweforce.state

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 6:27 AM
 * To change this template use File | Settings | File Templates.
 */
trait StateEditor[S <: State[_]] {

  /**
   * a mutable holder for a immutable object
   */
  var state : S

  /**
   * first change first, last change last
   */
  var changes =  List[Any]()

  def getBaselineState(): S = {
    state
  }

  def getUncommittedState(): S = {
    val uncommitState = state.applyChanges(changes)
    return uncommitState.asInstanceOf[S]
  }

  protected def applyChange(change : Any) = {
    changes = changes ++ List(change)
  }

}



