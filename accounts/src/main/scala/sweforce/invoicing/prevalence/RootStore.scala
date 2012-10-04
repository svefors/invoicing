package sweforce.invoicing.prevalence

import org.prevayler.{Prevayler, PrevaylerFactory}
import sweforce.invoicing.accounts.infrastructure.{Accounts}

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 9/24/12
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(1l)
class RootStore extends Serializable {


  var accountSettings = Map[Any, Accounts]()


}

object RootStore {

//  private lazy val instance = new RootStore

  private lazy val prevaylerFactory: PrevaylerFactory = new PrevaylerFactory() {
    configurePrevalentSystem(new RootStore)
//    configureTransactionFiltering(false)
  }

  lazy val prevayler = prevaylerFactory.create()

  def getInstance() : RootStore = {
    prevayler.prevalentSystem().asInstanceOf[RootStore];
  }

}





