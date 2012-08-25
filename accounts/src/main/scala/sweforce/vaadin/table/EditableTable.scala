package sweforce.vaadin.table

import com.vaadin.event.ItemClickEvent
import com.vaadin.data.Container
import vaadin.scala._
import vaadin.scala.TableFieldIngredients

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 8/16/12
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */

trait EditableTable extends Table with ItemClickEvent.ItemClickListener {

//  var editableColumns = Set[String]()

  this.itemClickListeners += (event =>{
    if (event.doubleClick) {
          if (!this.editable)
            this.editable = true
          editingItemId = event.itemId
          editingPropertyId = event.propertyId
          this.refreshRowCache()
        } else if (!event.doubleClick && this.editable) {
          editingItemId = null;
          editingPropertyId = null;
          this.editable = false;
        } else {
          //?
        }
  })
  tableFieldFactory_= (DefaultFieldFactory)


//  override def tableFieldFactory = super.tableFieldFactory

  override def tableFieldFactory_=(factory: TableFieldFactory) {
    super.tableFieldFactory_=(new WrappedFieldFactory(factory))
  }


  /*

   def tableFieldFactory: Option[TableFieldFactory] = wrapperFor[TableFieldFactory](p.getTableFieldFactory)
  def tableFieldFactory_=(factory: TableFieldFactory) = p.setTableFieldFactory(factory.p)
  def tableFieldFactory_=(factoryFunction: (TableFieldIngredients) => Option[Field]) = p.setTableFieldFactory(TableFieldFactory(factoryFunction).p)
  def tableFieldFactory_=(factory: Option[TableFieldFactory]) = factory match {
    case Some(factory) => p.setTableFieldFactory(factory.p)
    case None => p.setTableFieldFactory(null)
  }


   */
//  def editableColumn(columns: Seq[String]) {
//    columns.foreach(
//      editableColumns = editableColumns + _
//    )
//  }

  def itemClick(event: ItemClickEvent) = {

  }

  var editingPropertyId: Any = _;
  var editingItemId: Any = _;

  class WrappedFieldFactory(val inner: TableFieldFactory) extends TableFieldFactory {


    def createField(ingredients: TableFieldIngredients)  : Option[Field]= {
      if (ingredients.propertyId.equals(editingPropertyId) && ingredients.itemId.equals(editingItemId)){
        inner.createField(ingredients)
      }else{
        null;
      }
    }

//    override def createField(container: Container, itemId: AnyRef, propertyId: AnyRef, uiContext: Component): Field[_] = {
////      if (!editableColumns.contains(propertyId)) {
////        return null;
////      }
//      if (propertyId.equals(EditableTable.this.editingPropertyId) && itemId.equals(EditableTable.this.editingItemId)) {
//        return inner.createField(container, itemId, propertyId, uiContext)
//      } else {
//        return null;
//      }
//    }
  }


}
