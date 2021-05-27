package ua.kpi.comsys.IV8109.retrieved_jsons;

import java.util.ArrayList;

import ua.kpi.comsys.IV8109.model.Book;

public class BooksJson {
    String error;
    String total;
    ArrayList<Book> books;

    public ArrayList<Book> getBooks() {
        return books;
    }

}