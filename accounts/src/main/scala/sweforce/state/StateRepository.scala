package sweforce.state

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
trait StateRepository[S <: State[S]] {

  def get(identifier: Any): S

//  def put(identifier: Any, state: S)

}
