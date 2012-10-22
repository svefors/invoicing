package sweforce.vaadin.data

import com.vaadin.data.Item

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/7/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
trait UnitOfWork {

  def registerNew(item : Item)

  def registerClean(item : Item)

  def registerDirty(item : Item)

  def registerDeleted(item : Item)

  def commit()

  def rollback()
}
