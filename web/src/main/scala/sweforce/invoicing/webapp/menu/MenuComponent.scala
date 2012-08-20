package sweforce.invoicing.webapp.menu

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus
import vaadin.scala.{Label, Tree, VerticalLayout}
import com.vaadin.data.util.HierarchicalContainer
import sweforce.vaadin.scala.FProperty
import sweforce.gui.ap.place.{PlaceChangeEvent, Place}
import sweforce.invoicing.accounts.AccountsPlace
import vaadin.scala._
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 8/19/12
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */

class MenuComponent {

  object activity extends AbstractActivity {
    var place: Place = _;

    var started = false;

    def start(p1: Display, p2: EventBus) {
      if (!started) {
        view.tree.setContainerDataSource(menuContainer)
        view.tree.expandItemsRecursively("Entries")
        view.tree.expandItemsRecursively("Invoices")
        view.tree.expandItemsRecursively("Reports")
        view.tree.expandItemsRecursively("Settings")
        started = true
      }
      if (place.isInstanceOf[AccountsPlace]) {
        view.tree.setValue("SettingsAccounts")
      } else {
        view.tree.setValue(null)
      }
      p1.setView(view)
    }

    def setPlace(place: Place) {
      this.place = place;


    }


  }

  def handlesPlace(place: Place): Boolean = {
    if (place.isInstanceOf[AccountsPlace]) {
      true
    } else {
      false
    }
  }

  object menuContainer extends HierarchicalContainer {

    addContainerProperty("caption", classOf[String], "CAPTION MISSING")

    def addTreeItem(treeItem: TreeItem) {
      addItem(treeItem.caption).getItemProperty("caption").setValue(treeItem.caption)
      treeItem.children match {
        case Some(ch) =>
          ch.foreach(child => {
            addItem(treeItem.caption + child).getItemProperty("caption").setValue(child)
            setChildrenAllowed(treeItem.caption + child, false)
            setParent(treeItem.caption + child, treeItem.caption)
          })
        case None => {}
      }
    }


    this.addTreeItem(new TreeItem("Entries", Some(List("New", "Supplier Invoices", "Pending", "2012"))))
    this.addTreeItem(new TreeItem("Invoices", Some(List("Unsent", "2012", "Overdue"))))
    this.addTreeItem(new TreeItem("Reports", Some(List("Overview", "Ledger", "Balance sheet", "Profit/loss sheet", "Sales", "VAT"))))
    this.addTreeItem(new TreeItem("Settings", Some(List("Company", "Invoicing", "Products", "Accounts", "Tags", "Fiscal years", "VAT"))))

    class TreeItem(val caption: String, val children: Option[Seq[String]]) {
      def this(caption: String) = this(caption, None)
    }


  }

  object view extends VaadinView {
    lazy val rootContainer = new VerticalLayout(width = 200 px, height = 100 pct) {
      add(tree)
      add(bottom)
    }

    lazy val tree = new Tree() {
      setItemCaptionPropertyId("caption")
      setSelectable(true)
      setImmediate(true)
      addListener(new ValueChangeListener {
        def valueChange(event: ValueChangeEvent) {
          System.out.println(event.getProperty.getValue)
        }
      })
    }


    lazy val bottom = new VerticalLayout(width = 100 pct, height = 50 px)

    def asComponent() = rootContainer
  }

}



