package sweforce;

import com.vaadin.data.Container;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 7/20/12
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CleverTable extends Table implements ItemClickEvent.ItemClickListener, Table.ColumnReorderListener {

    private Object editingPropertyId;

    private Object editingItemId;

    private Set<Object> editableProperties = new HashSet<Object>();

    public CleverTable() {
    }

    public CleverTable(String caption) {
        super(caption);
    }

    public CleverTable(String caption, Container dataSource) {
        super(caption, dataSource);
    }



    @Override
    public void itemClick(ItemClickEvent event) {

        if (event.isDoubleClick()) {
            editProperty(event.getItemId(), event.getPropertyId());
        } else if (this.isEditable()) {
            stopEditing();
        }
        this.setEditable(true);
    }

    protected void stopEditing() {
        this.editingPropertyId = null;
        this.editingItemId = null;
        this.setEditable(false);
    }

    protected void editProperty(Object itemId, Object propertyId) {
        this.editingPropertyId = propertyId;
        this.editingItemId = itemId;
        this.setEditable(true);
    }

    private static Object nextPropertyid(Object[] propertyIds, Object propertyId, Set<Object> editableProperties){
        if (propertyIds[propertyIds.length -1] == propertyId)
            return null;
        else{
            int i = 0;
            for (Object p:propertyIds){
                if (p.equals(propertyId)){
                    for (int n = i+1; n < propertyIds.length; n++){
                        if (editableProperties.contains(propertyIds[n])){
                            return propertyIds[n];
                        }
                    }

                }
                i++;
            }
        }
        return null;
    }

    protected void editNextProperty() {
        if(editingItemId == null){
            editProperty(this.firstItemId(), getVisibleColumns()[0]);
        }else{

            editProperty(this.nextItemId(editingItemId), editingPropertyId);
        }
    }

    @Override
    public void columnReorder(ColumnReorderEvent event) {

    }

    private static class MyFieldFactory extends DefaultFieldFactory {

        private final Object propertyId;

        private final Object itemId;

        private MyFieldFactory(Object propertyId, Object itemId) {
            this.propertyId = propertyId;
            this.itemId = itemId;
        }

        @Override
        public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
            return super.createField(container, itemId, propertyId, uiContext);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
