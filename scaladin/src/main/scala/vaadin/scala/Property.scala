package vaadin.scala

object Property {
  def apply[T](value: T): Property[_] = new ObjectProperty[T](value)

  def unapply(property: Property[_]): Option[Any] = {
    if (property != null) property.value
    else None
  }
}

trait Property[T] extends Wrapper {
  def p: com.vaadin.data.Property[_]

  def value: Option[Any] = Option(p.getValue())
  def value_=(value: Option[Any]): Unit = value_=(value.orNull)
  def value_=(value: Any): Unit = p.setValue(value)
  def getType: Class[_] = p.getType // FIXME: _ <: T
  def readOnly: Boolean = p.isReadOnly
  def readOnly_=(ro: Boolean): Unit = p.setReadOnly(ro)
  override def toString: String = p.toString
}

trait PropertyViewer extends Wrapper {

  def p: com.vaadin.data.Property.Viewer

  def property: Option[Property[_]] = p.getPropertyDataSource() match {
    case p: com.vaadin.data.Property[_] => Some(new BasicProperty(p))
    case null => None
  }

  def property_=(property: Option[Property[_]]) = property match {
    case Some(prop) => p.setPropertyDataSource(prop.p)
    case None => p.setPropertyDataSource(null)
  }

  def property_=(property: Property[_]) = p.setPropertyDataSource(property.p)
}

trait PropertyEditor extends PropertyViewer {
  def p: com.vaadin.data.Property.Editor
}

/**
 * Basic property wrapper, wraps any instance of com.vaadin.data.Property
 */
class BasicProperty[T](override val p: com.vaadin.data.Property[_]) extends Property[T]

class ObjectProperty[T](value: T) extends Property[T] {
  val p = new com.vaadin.data.util.ObjectProperty[T](value)
}

class VaadinPropertyDelegator[T](scaladinProperty: Property[_]) extends com.vaadin.data.Property[T] {
  def getValue: T = scaladinProperty.value.get.asInstanceOf[T]
  def setValue(v: Any) = scaladinProperty.value = v
  def getType: Class[_ <: T] = null //scaladinProperty.getType // FIXME
  def isReadOnly = scaladinProperty.readOnly
  def setReadOnly(ro: Boolean) = scaladinProperty.readOnly = ro
}

class FunctionProperty[T](getter: Unit => T, setter: T => Unit = null)(implicit m: Manifest[T]) extends Property[T] {
  //delegate
  val p = new VaadinPropertyDelegator(this)

  override def value: Option[T] = Option(getter())

  override def value_=(value: Any) = setter(value.asInstanceOf[T])

  override def getType: Class[T] = m.erasure.asInstanceOf[Class[T]]

  override def readOnly: Boolean = setter != null

  override def readOnly_=(ro: Boolean): Unit = () // NOOP

  override def toString = "Value: " + value
}

class TextFileProperty(file: Option[java.io.File]) extends Property[String] {
  override val p: com.vaadin.data.util.TextFileProperty = new com.vaadin.data.util.TextFileProperty(file.orNull)
}