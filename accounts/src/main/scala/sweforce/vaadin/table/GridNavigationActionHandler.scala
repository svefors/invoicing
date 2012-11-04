package sweforce.vaadin.table

import sweforce.command.Command
import sweforce.vaadin.keyboard.{KeyBinding, CommandActionHandler, KeyGesture}

import sweforce.vaadin.keyboard.KeyGesture.DefaultImpl
import com.vaadin.event.Action
import com.vaadin.event.ShortcutAction.{ModifierKey, KeyCode}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/18/12
 * Time: 9:20 PM
 * To change this template use File | Settings | File Templates.
 */
class GridNavigationActionHandler(val gestures: GridNavigationActionHandler#Gestures) extends Action.Handler {

  def this() = this(
    new Gestures {
      def upKeyGesture = new KeyGesture.DefaultImpl("Up", KeyCode.ARROW_UP, ModifierKey.SHIFT)

      def prevKeyGesture = new KeyGesture.DefaultImpl("Previous", KeyCode.TAB, ModifierKey.SHIFT)

      def downKeyGesture = new KeyGesture.DefaultImpl("Down", KeyCode.ARROW_DOWN, ModifierKey.SHIFT)

      def nextKeyGesture = new KeyGesture.DefaultImpl("Next", KeyCode.TAB)
    }
  )

  var innerHandler: Action.Handler = CommandActionHandler.create();

  def bind(commands: GridNavigationActionHandler#Commands) = {
    innerHandler = CommandActionHandler.create(
      new KeyBinding.DefaultImpl(gestures.upKeyGesture, commands.up()),
      new KeyBinding.DefaultImpl(gestures.downKeyGesture, commands.down()),
      new KeyBinding.DefaultImpl(gestures.prevKeyGesture, commands.prev()),
      new KeyBinding.DefaultImpl(gestures.nextKeyGesture, commands.next())
    )
  }

  def getActions(target: Any, sender: Any) = {
    innerHandler.getActions(target, sender)
  }

  def handleAction(action: Action, sender: Any, target: Any) {
    innerHandler.handleAction(action, sender, target)
  }

  trait Gestures {

    def upKeyGesture: KeyGesture

    def downKeyGesture: KeyGesture

    def prevKeyGesture: KeyGesture

    def nextKeyGesture: KeyGesture

  }

  trait Commands {
    def up(): Command

    def down(): Command

    def next(): Command

    def prev(): Command
  }

}


