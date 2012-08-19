package sweforce.invoicing.webapp

import com.vaadin.ui.Root
import com.vaadin.terminal.WrappedRequest
import com.google.inject.{Provides, AbstractModule, Guice}
import sweforce.vaadin.security.SecureMvpModule
import sweforce.vaadin.security.shiro.ShiroSecurityModule
import sweforce.gui.ap.vaadin.VaadinModule
import sweforce.vaadin.security.login.LoginPlace
import sweforce.vaadin.security.logout.LogoutPlace
import sweforce.vaadin.layout.style2.Style2Layout
import sweforce.gui.event.EventBus
import com.google.inject.name.Names
import sweforce.gui.ap.place.{PlacesRunner, Place}
import collection.JavaConversions._
import sweforce.gui.ap.place.history.{PlaceTokenizerStore, PlaceHistoryModule}
import sweforce.gui.ap.place.history.PlaceTokenizerStore.Builder
import sweforce.invoicing.accounts.{AccountsPlaceTokenizer, AccountsPlace, ChartOfAccountsComponent}
import sweforce.gui.ap.activity.{ActivityManager, ActivityMapper}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/24/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */

class InvoicingVaadinRoot extends Root {

  val injector = Guice.createInjector(
    new VaadinModule(this),
    new ShiroSecurityModule(),
    new SecureMvpModule(),
    new PlaceHistoryModule(),
    myModule
  )

  object myModule extends AbstractModule {
    def configure() {
      bind(classOf[Place]).annotatedWith(Names.named("Default Place")).toInstance(new AccountsPlace());
    }

    @Provides
    def providePlaceTokenizerStore() = {
      new PlaceTokenizerStore.Builder().addPlace(
        classOf[LoginPlace]).addPlace(
        classOf[LogoutPlace]).addTokenizer(
        classOf[AccountsPlaceTokenizer]
      ).build();
    }

    //    @Provides
    //    @Named(PlaceHistoryModule.NAMED_PLACE_CLASSES) def providePlaceClasses: java.util.Collection[Class[_ <: Place]] = {
    //
    //      var places: List[Class[_ <: Place]] =  List[Class[_ <: Place]](
    //      classOf[LoginPlace],
    //        classOf[LogoutPlace],
    //        classOf[AccountsPlace])
    //      return places
    //    }
    //    @Provides
    //    @Named(PlaceHistoryModule.NAMED_PLACE_CLASSES)
    //    def providePlaceClasses() : java.util.Collection[classOf[Place]]{
    //      val classes = List(
    //        classOf[LoginPlace],
    //        classOf[LogoutPlace],
    //        classOf[AccountsPlace]
    //      )
    //      classes
    //    }
    //    @Provides
    //            @Named(PlaceHistoryModule.NAMED_PLACE_CLASSES)
    //            Collection<Class<? extends Place>> providePlaceClasses() {
    //                //TODO write a classpath scanning mechanism
    //                List<Class<? extends Place>> places = new ArrayList<Class<? extends Place>>();
    //                places.add(LoginPlace.class);
    //                places.add(LogoutPlace.class);
    //                places.add(Role1Place.class);
    //                places.add(NorolePlace.class);
    //                places.add(Role2Place.class);
    //                return places;
    //            }
  }

  def init(request: WrappedRequest) {
    val style2Layout = new Style2Layout()
    this.setContent(style2Layout)
    val eventbus = injector.getInstance(classOf[EventBus])

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
    //    centralActivityMapper.
  }

  object centralActivityMapper extends ActivityMapper {
    val chartOfAccountsComponent = new ChartOfAccountsComponent

    def getActivity(p1: Place) = {
      chartOfAccountsComponent.activity
    }
  }

  object headerActivityMapper extends ActivityMapper {
    def getActivity(p1: Place) = null
  }

  object leftActivityMapper extends ActivityMapper {
    def getActivity(p1: Place) = null
  }

  object rightActivityMapper extends ActivityMapper {
    def getActivity(p1: Place) = null
  }

}
