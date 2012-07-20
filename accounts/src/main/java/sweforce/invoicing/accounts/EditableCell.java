package sweforce.invoicing.accounts;

import com.vaadin.event.*;
import com.vaadin.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 7/18/12
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditableCell extends Panel implements LayoutEvents.LayoutClickListener, MouseEvents.DoubleClickListener{

    private AbstractComponent viewComponent;

    private AbstractComponent editComponent;

    public EditableCell(Table table, Object itemId, Object propertyId) {
        super(new VerticalLayout());
        viewComponent = createViewComponent();
        editComponent = createEditComponent();
        this.addComponent(viewComponent);
        ((VerticalLayout)this.getContent()).addListener((LayoutEvents.LayoutClickListener) this);

        this.addAction(new ShortcutListener("", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                save();
            }
        });
        this.addAction(new ShortcutListener("", ShortcutAction.KeyCode.TAB, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                save();
            }
        });
    }

    protected void save(){
        showView();
        //go to next cell?
        /*
        something.nextEditableCell().showEdit().
         */
        next();
    }

    protected void next(){
        //set focus on next cell

//        navigator.gotoNext();
    }

    private CellNavigator navigator;

    public static interface CellNavigator{

        public void gotoNext(Object currentItemId, Object currentPropertyId);

        public void gotoPrevious(Object currentItemId, Object currentPropertyId);

    }

    protected void cancel(){
        this.showView();
        //set focus on row
    }


    @Override
    public <T extends Action & Action.Listener> void addAction(T action) {
        super.addAction(action);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getSource() == viewComponent && event.isDoubleClick()){
            this.showEdit();
        }
    }

    @Override
    public void doubleClick(MouseEvents.DoubleClickEvent event) {
        if (event.getSource() == viewComponent){
            this.showEdit();
        }
    }

    //    @Override
//    public void doubleClick(MouseEvents.DoubleClickEvent event) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    private void showEdit(){
        this.replaceComponent(viewComponent, editComponent);
    }

    private void showView(){
        this.replaceComponent(editComponent, viewComponent);
    }

    /**
     * return component used to view
     * @return
     */
    protected Label createViewComponent(){
        return new Label();
    }

    /**
     * return component used to view
     * @return
     */
    protected TextField createEditComponent(){
        return new TextField();
    }

}
