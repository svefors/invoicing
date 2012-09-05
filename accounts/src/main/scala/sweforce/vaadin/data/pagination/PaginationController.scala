package sweforce.vaadin.data.pagination

import com.vaadin.data.Container.{ItemSetChangeEvent, ItemSetChangeListener}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}


/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 9/3/12
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
class PaginationController(val paginationContainer: PaginationContainer) extends ItemSetChangeListener with ValueChangeListener {

  var currentPage: Int = 0

  var rowsPerPage: Int = 20

  /**
   * when an
   * @param event
   */
  def containerItemSetChange(event: ItemSetChangeEvent) = {

  }

  /**
   * detects if the
   * @param event
   */
  def valueChange(event: ValueChangeEvent) {
    val itemId = event.getProperty.getValue

  }

  def forward(pages: Int) = {
    currentPage = currentPage + pages

  }

  def back(pages : Int) = {

  }

  def last() = {

  }

  def first() = {

  }

}
