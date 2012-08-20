package sweforce.invoicing.webapp.toolbar

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import vaadin.scala.HorizontalLayout
import vaadin.scala._
/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 8/19/12
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */

class ToolbarComponent {

  object activity extends AbstractActivity {

    def start(panel: Display, eventBus: EventBus) {
      panel.setView(view)
    }

  }

  object view extends VaadinView {

    lazy val rootContainer = new HorizontalLayout(height = 100 pct){
      setSpacing(true)
      setMargin(true)
      add(new Button(caption = "Print"))
      add(new Button(caption = "New invoice"))
      add(new Button(caption = "New entry"))

    }

    def asComponent() = rootContainer

  }

}
