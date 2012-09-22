package sweforce.invoicing.webapp.toolbar

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import com.vaadin.ui.HorizontalLayout

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

    lazy val rootContainer = new HorizontalLayout(){
      setSpacing(true)
      setMargin(true)
//      width = (100 percent)
//      height = (100 percent)
//      spacing = true
//      margin = true
//      add(Button("Print", {System.out.println("Print Clicked")} ))
//      add(Button("New invoice", {System.out.println("New Invoice Clicked")} ))
//      add(Button("New entry", {System.out.println("New Entry Clicked")} ))

    }

    def asComponent() = rootContainer

  }

}
