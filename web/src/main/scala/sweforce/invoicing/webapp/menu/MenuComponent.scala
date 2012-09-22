package sweforce.invoicing.webapp.menu

import sweforce.gui.ap.activity.AbstractActivity
import sweforce.gui.display.{VaadinView, Display}
import sweforce.gui.event.EventBus

import sweforce.gui.ap.place.{PlaceChangeEvent, Place}
import sweforce.invoicing.accounts.AccountsPlace
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.data.util.HierarchicalContainer
import com.vaadin.ui.{Alignment, Tree, VerticalLayout}
import javax.inject.{Inject, Singleton}
import sweforce.gui.ap.place.controller.PlaceController
import reflect.BeanProperty
import sweforce.invoicing.entries.create.CreateEntryPlace

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 8/19/12
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
class MenuComponent(val placeController: PlaceController) {


  object activity extends AbstractActivity with PlaceChangeEvent.Handler{
    var place: Place = _;

    var started = false;


    override def onStop() {
      super.onStop()
    }


    def onPlaceChange(event: PlaceChangeEvent) {
      view.tree.setValue(PlaceMenuItem(event.getNewPlace))
    }

    def start(p1: Display, p2: EventBus) {
      if (!started) {
        view.tree.setContainerDataSource(menuContainer)
        view.tree.expandItemsRecursively(theTree.Entries)
        view.tree.expandItemsRecursively(theTree.Invoices)
        view.tree.expandItemsRecursively(theTree.Reports)
        view.tree.expandItemsRecursively(theTree.Settings)
        p2.addHandler(classOf[PlaceChangeEvent], this)
        view.tree.addValueChangeListener(new ValueChangeListener {
          def valueChange(event: ValueChangeEvent) {
            if (event.getProperty.getValue != null && event.getProperty.getValue.isInstanceOf[MenuItem]) {
              event.getProperty.getValue.asInstanceOf[MenuItem] match {
                case PlaceMenuItem(place) => placeController.goTo(place)
                case NoActionMenuItem(caption) => {
                  System.out.println(caption)
                }
              }
            }
          }
        })
        started = true
      }
      if (place != null)
        view.tree.setValue(PlaceMenuItem(place))
      else
        view.tree.setValue(theTree.Entries)

      p1.setView(view)
    }


    def setPlace(place: Place) {
      this.place = place;


    }


  }

  def handlesPlace(place: Place): Boolean = {
    if (place.isInstanceOf[AccountsPlace]
      || place.isInstanceOf[CreateEntryPlace]
    ) {
      true
    } else {
      false
    }
  }


  //  class PlaceNavigator(val place : Place, @BeanProperty val caption : String) {
  //
  //    def navigate() = {
  //      placeController.goTo(place);
  //    }
  //
  //
  //  }

  trait Placeable {
    val place: Place
  }


  sealed trait MenuItem {
    //def getChildren: Seq[MenuItem]

    //    def this(caption: String) = this(caption, Seq[MenuItem]())
  }

  trait Parent[T] {
    def getChildren(): Seq[T]
  }

  case class NoActionMenuItem(val caption: String) extends MenuItem

  case class PlaceMenuItem(val place: Place) extends MenuItem with Placeable {
  }

  implicit def toNoActionMenuItem(caption: String): NoActionMenuItem = {
    NoActionMenuItem(caption)
  }

  implicit def toPlaceMenuItem(place: Place): PlaceMenuItem = {
    PlaceMenuItem(place)
  }


  object theTree {

    object Entries extends NoActionMenuItem("Entries") with Parent[MenuItem] {

      def getChildren() = {
        List[MenuItem](
          (CreateEntryPlace()),
          "Supplier Invoices",
          "Pending"
        )
      }

      //TODO understand how to add years?

    }

    object Invoices extends NoActionMenuItem("Invoices") with Parent[MenuItem] {

      def getChildren() = {
        List[MenuItem](
          ("Unsent"),
          ("Overdue"),
          ("2012")
        )
      }
    }

    object Reports extends NoActionMenuItem("Reports") with Parent[MenuItem] {

      def getChildren() = {
        List[MenuItem](
          ("Overview"),
          ("Ledger"),
          ("Balance sheet"),
          ("Profit/loss sheet"),
          ("Sales"),
          ("VAT")
        )
      }
    }

    object Settings extends NoActionMenuItem("Settings") with Parent[MenuItem] {

      def getChildren() = {
        List[MenuItem](
          ("Company"),
          ("Invoicing"),
          ("Products"),
          (AccountsPlace()),
          ("Tags"),
          ("Fiscal years"),
          ("VAT")
        )
      }
    }

  }

  val placeCaptionMap = Map[Place, String](
    AccountsPlace() -> "Accounts",
    CreateEntryPlace() -> "New"
  )

  object menuContainer extends HierarchicalContainer {

    addContainerProperty("caption", classOf[String], "CAPTION MISSING")


    def addMenuItem(menuItem: MenuItem) {
      this.addItem(menuItem)
      menuItem match {
        case NoActionMenuItem(caption) => this.getContainerProperty(menuItem, "caption").setValue(caption)
        case PlaceMenuItem(place) => this.getContainerProperty(menuItem, "caption").setValue(placeCaptionMap(place))
      }
      if (menuItem.isInstanceOf[Parent[MenuItem]]) {
        menuItem.asInstanceOf[Parent[MenuItem]].getChildren().foreach(child => {
          addMenuItem(child)
          this.setParent(child, menuItem)
        })
      } else {
        setChildrenAllowed(menuItem, false)
      }
    }

    this.addMenuItem(theTree.Entries)
    this.addMenuItem(theTree.Invoices)
    this.addMenuItem(theTree.Reports)
    this.addMenuItem(theTree.Settings)
    //    def addTreeItem(treeItem: TreeItem) {
    //      addItem(treeItem.caption).getItemProperty("caption").setValue(treeItem.caption)
    //      treeItem.children.foreach(child => {
    //        addItem(treeItem.caption + child).getItemProperty("caption").setValue(child)
    //        setChildrenAllowed(treeItem.caption + child, false)
    //        setParent(treeItem.caption + child, treeItem.caption)
    //      })
    //    }
    //
    //
    //    this.addTreeItem(new TreeItem("Entries", List("New", "Supplier Invoices", "Pending", "2012")))
    //    this.addTreeItem(new TreeItem("Invoices", List("Unsent", "2012", "Overdue")))
    //    this.addTreeItem(new TreeItem("Reports", List("Overview", "Ledger", "Balance sheet", "Profit/loss sheet", "Sales", "VAT")))
    //    this.addTreeItem(new TreeItem("Settings", List("Company", "Invoicing", "Products", "Accounts", "Tags", "Fiscal years", "VAT")))
    //
    //    class TreeItem(val caption: String, val children: Seq[String]) {
    //      def this(caption: String) = this(caption, Seq[String]())
    //    }
    //

  }

  object view extends VaadinView {
    lazy val rootContainer = new VerticalLayout() {
      setWidth("100%")
      setHeight("100%")
      addComponents(tree, bottom)
      setExpandRatio(tree, 1.0f)
      setComponentAlignment(tree, Alignment.TOP_LEFT)
      setComponentAlignment(bottom, Alignment.BOTTOM_LEFT)
    }

    lazy val tree = new Tree() {
      setItemCaptionPropertyId("caption")
      setSelectable(true)
      setMultiSelect(false)
      setImmediate(true)
      setNullSelectionAllowed(false)
      addValueChangeListener(new ValueChangeListener {
        def valueChange(event: ValueChangeEvent) {
          //          if ()
        }
      })
    }


    lazy val bottom = new VerticalLayout() {
      setWidth("100%")
      setHeight("50px")
      //      width_=(100 pct)
      //      height_=(50 px)
    }

    def asComponent() = rootContainer
  }

}



