package sweforce.invoicing.webapp

import com.vaadin.ui.Root
import com.vaadin.terminal.WrappedRequest
import com.google.inject.{Provides, AbstractModule, Guice}
import sweforce.gui.ap.place.history.PlaceHistoryModule
import sweforce.vaadin.security.SecureMvpModule
import sweforce.vaadin.security.shiro.ShiroSecurityModule
import sweforce.gui.ap.vaadin.VaadinModule
import javax.inject.Named
import sweforce.vaadin.security.login.LoginPlace
import sweforce.vaadin.security.logout.LogoutPlace
import sweforce.gui.ap.activity.{ActivityManager, ActivityMapper}
import sweforce.gui.ap.place.Place
import sweforce.vaadin.layout.style2.Style2Layout
import sweforce.invoicing.accounts.ChartOfAccountsComponent
import sweforce.gui.event.EventBus
import com.google.inject.name.Names
import sweforce.invoicing.accounts.ChartOfAccountsComponent
import sweforce.invoicing.accounts.ChartOfAccountsComponent.AccountsPlace

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
      bind(Place.class).annotatedWith(Names.named("Default Place")).toInstance(new ChartOfAccountsComponent.AccountsPlace());
    }

    @Provides
    @Named(PlaceHistoryModule.NAMED_PLACE_CLASSES)
    def providePlaceClasses(){
      List(
        classOf[LoginPlace],
        classOf[LogoutPlace]
      )
    }
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
    centralActivityMapper.
  }

  object centralActivityMapper extends ActivityMapper {
    chartOfAccountsComponent = new ChartOfAccountsComponent
    def getActivity(p1: Place) = {
      chartOfAccountsComponent
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
