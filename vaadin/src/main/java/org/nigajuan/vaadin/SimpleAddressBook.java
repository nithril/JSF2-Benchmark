package org.nigajuan.vaadin;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.nigajuan.Book;
import org.nigajuan.DataTableBean;

public class SimpleAddressBook extends Application {

    private static String[] fields = { "isbn", "author", "title"};

    private DataTableBean dataTableBean = new DataTableBean();

    private Table contactList = new Table();

    private HorizontalLayout bottomLeftCorner = new HorizontalLayout();
    private IndexedContainer addressBookData = createDummyData();

    @Override
    public void init() {
        initLayout();
        initAddressList();
    }

    private void initLayout() {

        VerticalLayout left = new VerticalLayout();
        left.setSizeFull();
        left.addComponent(contactList);
        contactList.setSizeFull();
        left.setExpandRatio(contactList, 1);

        setMainWindow(new Window("Address Book", left));

        bottomLeftCorner.setWidth("100%");
        left.addComponent(bottomLeftCorner);
    }



    private String[] initAddressList() {
        contactList.setContainerDataSource(addressBookData);
        contactList.setVisibleColumns(fields);
        contactList.setSelectable(true);
        contactList.setImmediate(true);
        return fields;
    }



    private IndexedContainer createDummyData() {


        IndexedContainer ic = new IndexedContainer();

        for (String p : fields) {
            ic.addContainerProperty(p, String.class, "");
        }

        // Create dummy data by randomly combining first and last names
        for (int i = 0; i < dataTableBean.getBooks().size() ; i++) {
            
            Book book = dataTableBean.getBooks().get(i);
            
            Object id = ic.addItem();
            ic.getContainerProperty(id, "isbn").setValue(book.getIsbn());
            ic.getContainerProperty(id, "author").setValue(book.getAuthor());
            ic.getContainerProperty(id, "title").setValue(book.getTitle());
        }

        return ic;
    }

}