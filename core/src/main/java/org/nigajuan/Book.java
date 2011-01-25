package org.nigajuan;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 12/12/10
 * Time: 00:28
 * To change this template use File | Settings | File Templates.
 */
public class Book {

    private String isbn;
    private String author;
    private String title;


    public Book(String isbn, String author, String title) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
