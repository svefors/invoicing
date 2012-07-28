package sweforce.vaadin.scala

import com.vaadin.ui.Component

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/24/12
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */

trait VaadinView extends Component with sweforce.gui.display.VaadinView{
  def asComponent() = this
}
