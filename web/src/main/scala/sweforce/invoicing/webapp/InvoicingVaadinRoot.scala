package sweforce.invoicing.webapp

import com.vaadin.server.{VaadinRequest, Sizeable}

import com.google.inject.{Provides, AbstractModule, Guice}

import menu.MenuComponent
import sweforce.vaadin.security.SecureMvpModule
import sweforce.vaadin.security.shiro.ShiroSecurityModule
import sweforce.gui.ap.vaadin.VaadinModule
import sweforce.vaadin.security.login.{UserLoginSuccessEvent, LoginActivity, LoginPlace}
import sweforce.vaadin.security.logout.{LogoutActivity, LogoutPlace}
import sweforce.vaadin.layout.style2.Style2Layout
import sweforce.event.EventBus
import com.google.inject.name.Names
import sweforce.gui.ap.place.{PlaceChangeEvent, PlaceChangeRequestEvent, PlacesRunner, Place}
import collection.JavaConversions._
import sweforce.gui.ap.place.history.{PlaceTokenizerStore, PlaceHistoryModule}
import sweforce.gui.ap.place.history.PlaceTokenizerStore.Builder
import sweforce.gui.ap.activity.{Activity, ActivityManager, ActivityMapper}
import sweforce.vaadin.security.login.UserLoginSuccessEvent.Handler
import sweforce.invoicing.accounts.gui.{ChartOfAccountsGUI, AccountsPlaceTokenizer, AccountsPlace}
import com.vaadin.ui.{UI}
import com.vaadin.annotations.{PreserveOnRefresh, Theme}
import sweforce.gui.ap.place.controller.PlaceController
import sweforce.invoicing.entries.create.{CreateEntryGUI, CreateEntryPlace, CreateEntryPlaceTokenizer}
import toolbar.ToolbarComponent
import sweforce.invoicing.accounts.app.AccountsFactory
import grizzled.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/24/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Theme("invoicing")
@PreserveOnRefresh
class InvoicingVaadinRoot extends UI with Logging {

  AccountsFactory.ensureDemoDataIsCreated()

  val injector = Guice.createInjector(
    new VaadinModule(this),
    new ShiroSecurityModule(),
    new SecureMvpModule(),
    new PlaceHistoryModule(),
    myModule
  )

  object myModule extends AbstractModule {
    def configure() {
      bind(classOf[Place]).annotatedWith(Names.named("Default Place")).toInstance(AccountsPlace());
    }

    @Provides
    def providePlaceTokenizerStore() = {
      new PlaceTokenizerStore.Builder()
        .addPlace(classOf[LoginPlace])
        .addPlace(classOf[LogoutPlace])
        .addTokenizer(classOf[AccountsPlaceTokenizer])
        .addTokenizer(classOf[CreateEntryPlaceTokenizer])
        .build();
    }

  }


  def init(request: VaadinRequest) {
    debug("init")
    //    this.set
    val style2Layout = new Style2Layout()
    style2Layout.setLeftDisplaySize(150, Sizeable.Unit.PIXELS)
    this.setContent(style2Layout)
    val eventbus = injector.getInstance(classOf[EventBus])
    //TODO sort this out!
    eventbus.addHandler(classOf[UserLoginSuccessEvent], new Handler {
      def onAfterLogin(wantedPlace: Place) {
        if (wantedPlace != null) {
          eventbus.fireEvent(new PlaceChangeEvent(wantedPlace))
        } else {
          eventbus.fireEvent(new PlaceChangeEvent(AccountsPlace()))
        }
      }
    })
    val centralActivityManager = new ActivityManager(centralActivityMapper, eventbus)
    centralActivityManager.setDisplay(style2Layout.getCenterDisplay)

    val headerActivityManager = new ActivityManager(headerActivityMapper, eventbus)
    headerActivityManager.setDisplay(style2Layout.getHeaderDisplay)

    val leftActivityManager = new ActivityManager(leftActivityMapper, eventbus)
    leftActivityManager.setDisplay(style2Layout.getLeftDisplay)

    val rightActivityManager = new ActivityManager(rightActivityMapper, eventbus)
    rightActivityManager.setDisplay(style2Layout.getRightDisplay)

    this.setContent(style2Layout)
    val placesRunner: PlacesRunner = injector.getInstance(classOf[PlacesRunner])

    try {
      placesRunner.start
    }
    catch {
      case t: Throwable => {
        t.printStackTrace
      }
    }
    debug("init-end")
  }

  object centralActivityMapper extends ActivityMapper {
    def getActivity(p1: Place): Activity = {
      if (p1.isInstanceOf[LoginPlace]) {
        injector.getInstance(classOf[LoginActivity])
      } else if (p1.isInstanceOf[LoginPlace]) {
        injector.getInstance(classOf[LogoutActivity])
      } else {
        null
      }
    }
  }

  object headerActivityMapper extends ActivityMapper {
    val toolbarComponent = new ToolbarComponent

    def getActivity(p1: Place): Activity = {
      if (!p1.isInstanceOf[LoginPlace] && !p1.isInstanceOf[LogoutPlace]) {
        toolbarComponent.activity
      } else {
        null
      }
    }
  }

  object leftActivityMapper extends ActivityMapper {

    val menuComponent = new MenuComponent(injector.getInstance(classOf[PlaceController]))

    def getActivity(p1: Place): Activity = {
      if (menuComponent.handlesPlace(p1)) {
        menuComponent.activity.setPlace(p1)
        menuComponent.activity
      } else {
        null
      }
    }
  }

  object rightActivityMapper extends ActivityMapper {

    val chartOfAccountsComponent = new ChartOfAccountsGUI

    val createNewEntriesComponent = new CreateEntryGUI

    def getActivity(p1: Place): Activity = {
      if (p1.isInstanceOf[AccountsPlace]) {
        chartOfAccountsComponent.activity
      } else if (p1.isInstanceOf[CreateEntryPlace]){
        createNewEntriesComponent.activity
      } else{
        null
      }
    }
  }

}
