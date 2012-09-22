package vaadin.scala

import scala.collection.mutable
import vaadin.scala.mixins.HasComponentsMixin
import vaadin.scala.mixins.AbstractComponentContainerMixin
import vaadin.scala.mixins.AbstractLayoutMixin
import vaadin.scala.mixins.ComponentContainerMixin
import vaadin.scala.mixins.LayoutMixin

package mixins {
  trait HasComponentsMixin extends ComponentMixin
  trait ComponentContainerMixin extends HasComponentsMixin
  trait AbstractComponentContainerMixin extends AbstractComponentMixin with ComponentContainerMixin
  trait LayoutMixin extends ComponentContainerMixin
  trait AbstractLayoutMixin extends AbstractComponentContainerMixin with LayoutMixin
}

trait HasComponents extends Component {

  override def p: com.vaadin.ui.HasComponents with HasComponentsMixin

  def componentVisible(childComponent: Component) = p.isComponentVisible(childComponent.p)

  def components: Iterable[Component] = new Iterable[Component] {
    def iterator: Iterator[Component] = {
      import scala.collection.JavaConverters._
      p.iterator.asScala.map(wrapperFor[Component](_).get)
    }
  }
}

trait ComponentContainer extends HasComponents {

  override def p: com.vaadin.ui.ComponentContainer with ComponentContainerMixin with HasComponentsMixin
  // provide add or addComponent or both?
  def add[C <: Component](component: C): C = {
    p.addComponent(component.p)
    component
  }
  def addComponent[C <: Component](component: C): C = add(component)

  def removeComponent(c: Component): Unit = {
    p.removeComponent(c.p)
  }

  def removeAllComponents(): Unit = {
    p.removeAllComponents()
  }

  def replaceComponent(oldComponent: Component, newComponent: Component): Unit = {
    p.replaceComponent(oldComponent.p, newComponent.p)
  }

  def moveComponentsFrom(source: ComponentContainer): Unit = {
    p.moveComponentsFrom(source.p)
  }

  override def components: mutable.Set[Component] = new mutable.Set[Component] {
    import scala.collection.JavaConversions.asScalaIterator
    def contains(key: Component) = {
      p.iterator.contains(key.p)
    }
    def iterator: Iterator[Component] = {
      p.iterator.map(wrapperFor[Component](_).get)
    }
    def +=(elem: Component) = { addComponent(elem); this }
    def -=(elem: Component) = { removeComponent(elem); this }
  }

  // TODO: listeners
}

abstract class AbstractComponentContainer(override val p: com.vaadin.ui.AbstractComponentContainer with AbstractComponentContainerMixin) extends AbstractComponent(p) with ComponentContainer

case class Margin(top: Boolean = false, right: Boolean = false, bottom: Boolean = false, left: Boolean = false)

trait Layout extends ComponentContainer {

  override def p: com.vaadin.ui.Layout with LayoutMixin

}

abstract class AbstractLayout(override val p: com.vaadin.ui.AbstractLayout with AbstractLayoutMixin) extends AbstractComponentContainer(p) with Layout {

}

trait SpacingHandler {

  def p: com.vaadin.ui.Layout.SpacingHandler

  def spacing = p.isSpacing()
  def spacing_=(spacing: Boolean) = p.setSpacing(spacing)
}

trait AlignmentHandler {

  def p: com.vaadin.ui.Layout.AlignmentHandler

  def alignment(component: Component) = Alignment(p.getComponentAlignment(component.p).getBitMask())

  def alignment(component: Component, alignment: Alignment.Value) = p.setComponentAlignment(component.p, new com.vaadin.ui.Alignment(alignment.id))
  def alignment(componentAlignment: Tuple2[Component, Alignment.Value]) = p.setComponentAlignment(componentAlignment._1.p, new com.vaadin.ui.Alignment(componentAlignment._2.id))
}

trait MarginHandler {

  def p: com.vaadin.ui.Layout.MarginHandler

  def margin: Margin = {
    val margin = p.getMargin()
    Margin(margin.hasTop, margin.hasRight, margin.hasBottom, margin.hasLeft)
  }
  def margin_=(margin: Boolean): Unit = p.setMargin(margin)
  def margin_=(margin: Margin): Unit = p.setMargin(new com.vaadin.shared.ui.MarginInfo(margin.top, margin.right, margin.bottom, margin.left))
  def margin(top: Boolean = false, right: Boolean = false, bottom: Boolean = false, left: Boolean = false) { margin = (Margin(top, right, bottom, left)) }
}