package sweforce.state.prevayler

import sweforce.state.{StateRepository, State}
import org.prevayler.Transaction

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
trait PrevaylerStateRepository[S <: State[S]] extends StateRepository[S] {

  /*
  should be responsible to create the correct transaction
  but how?
   */




}
