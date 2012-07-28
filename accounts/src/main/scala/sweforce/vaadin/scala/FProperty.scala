package sweforce.vaadin.scala

import com.vaadin.data.Property

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/23/12
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */

class FProperty[T](getter: => T, setter: (T) => Unit)(implicit m: Manifest[T]) extends Property[T] with Serializable {

  def this(getter: => T)(implicit m: Manifest[T]) = this(getter, null)

  override def getValue = getter

  override def setValue(newValue: Any) {
    setter(newValue.asInstanceOf[T])
  }

  //  def getType = m.erasure

  override def getType: Class[T] = m.erasure.asInstanceOf[Class[T]]

  override def isReadOnly = {
    readOnly
  }

  private var readOnly = setter == null

  override def setReadOnly(newStatus: Boolean) {
    if (setter != null) {
      readOnly = true
    }
  }

  override def toString = "Value: " + getValue

}

object FProperty {

  //  def apply[T](getter: => T) = {
  //    new FProperty[T](getter)
  //  }
  //
  //  def apply[T](getter: => T, setter: (T) => Unit) = {
  //    new FProperty[T](getter, setter)
  //  }
}

