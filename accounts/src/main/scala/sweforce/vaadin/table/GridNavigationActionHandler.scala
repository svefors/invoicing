package sweforce.vaadin.table

import com.vaadin.event.{ShortcutAction, Action}
import sweforce.command.Command
import sweforce.vaadin.keyboard.KeyGesture

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/18/12
 * Time: 9:20 PM
 * To change this template use File | Settings | File Templates.
 */
class GridNavigationActionHandler(val viewModel : GridNavigationGestures) extends Action.Handler{

  val innerHandler = CommandActionHandler

}


/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/18/12
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
trait GridNavigationGestures {

  def upKeyGesture : KeyGesture

  def downKeyGesture : KeyGesture

  def leftKeyGesture : KeyGesture

  def rightKeyGesture : KeyGesture

}


