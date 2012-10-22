package sweforce.vaadin.table

import com.vaadin.ui.{Field, Component, TableFieldFactory}
import com.vaadin.data.{Validator, Container}
import com.vaadin.event.FieldEvents.{BlurEvent, BlurListener, BlurNotifier}
import sweforce.vaadin.table.ValidationOnBlurFieldFactory.ViewModel

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/20/12
 * Time: 8:19 AM
 * To change this template use File | Settings | File Templates.
 */
class ValidationOnBlurFieldFactory(val inner: TableFieldFactory) extends TableFieldFactory {

  trait ViewModel {

    def getValidator(itemId : Any, propertyId : Any) : Validator

  }
  var viewModel : ViewModel;

  def createField(container: Container, itemId: Any, propertyId: Any, uiContext: Component) = {
    val field = inner.createField(container, itemId, propertyId, uiContext)
    if (field != null && field.isInstanceOf[BlurNotifier]){
      field.asInstanceOf[BlurNotifier].addBlurListener(new BlurListener {
        def blur(event: BlurEvent) {
          field.addValidator(ValidationOnBlurFieldFactory.this.viewModel.getValidator)
        }
      })
    }
    field
  }
}
