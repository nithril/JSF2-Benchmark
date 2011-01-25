package org.nigajuan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.model.ListDataModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 12/12/10
 * Time: 00:28
 * To change this template use File | Settings | File Templates.
 */
public class DataTableBean {

    private static Logger log = LoggerFactory.getLogger(DataTableBean.class);

    private List<Book> books = new ArrayList<Book>();
    private ListDataModel booksModel;

    private String beanName;

    public DataTableBean() {
        log.error("DataTableBean");
        for (int i = 0; i < 10000 ; i++) {
            books.add(new Book("isbn" + i, "author" + i, "title" + i));
        }
        booksModel = new ListDataModel(books);
    }

    public void addBooks(){
        for (int i = 0; i < 10000 ; i++) {
            //books.add(new Book("isbn" + i, "author" + i, "title" + i));
            books.remove(books.size()-1);
        }
    }

    public List<Book> getBooks() {
        //log.error(beanName);
        return books;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public ListDataModel getBooksModel() {
        return booksModel;
    }

    public void setBooksModel(ListDataModel booksModel) {
        this.booksModel = booksModel;
    }
}
