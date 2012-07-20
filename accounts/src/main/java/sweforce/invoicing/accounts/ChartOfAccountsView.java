package sweforce.invoicing.accounts;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;
import sweforce.gui.display.VaadinView;

import java.awt.event.FocusListener;

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 7/17/12
 * Time: 11:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChartOfAccountsView extends VerticalLayout implements VaadinView {

    private HorizontalLayout bottomToolBar;

    private Table accounts;

    @Override
    public Component asComponent() {
        return this;
    }

    public ChartOfAccountsView() {
        buildMainLayout();
    }

    protected void buildMainLayout(){
        setSizeFull();
        accounts = buildAccountsTable();
        bottomToolBar = new HorizontalLayout();

    }

    protected Table buildAccountsTable(){
        Table accounts = new Table();
        accounts.addGeneratedColumn("accountnr", new Table.ColumnGenerator(){
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return new AccountNrCell(source, itemId, columnId);
            }
        });
//        accounts.addContainerProperty()
        return accounts;
    }

    private static class AccountNrCell extends CustomComponent implements LayoutEvents.LayoutClickListener{
        private Table source;
        private Object itemId;
        private Object columnId;

        private AbsoluteLayout compositionRoot;
        private Label view = new Label();
        private TextField edit = new TextField();

        private AccountNrCell(Table source, Object itemId, Object columnId) {
            this.source = source;
            this.itemId = itemId;
            this.columnId = columnId;
            compositionRoot = new AbsoluteLayout();
            compositionRoot.setWidth("100%");
            compositionRoot.setHeight("100%");
            this.setCompositionRoot(compositionRoot);
            compositionRoot.addComponent(view);
            compositionRoot.addListener(this);
            edit.addListener(new FieldEvents.BlurListener(){
                @Override
                public void blur(FieldEvents.BlurEvent event) {
                    saveEdit();
                }
            });

        }

        private void showEdit(){
            view.setVisible(false);
            view.setSizeUndefined();
            edit.setVisible(true);
            edit.setValue(view.getValue());
            edit.setSizeFull();
        }

        private void showView(){
            edit.setVisible(false);
            edit.setSizeUndefined();
            view.setVisible(true);
            view.setValue(edit.getValue());
            view.setSizeFull();
        }

        private void saveEdit(){
            showView();
        }


        @Override
        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
            if (event.getClickedComponent() == view){
                showEdit();
            }else{
                showView();
            }

        }
    }

}
