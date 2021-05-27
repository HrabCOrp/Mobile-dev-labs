package ua.kpi.comsys.IV8109.ui.books;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.activities.ShowDesc;
import ua.kpi.comsys.IV8109.adapters.BookAdapter;
import ua.kpi.comsys.IV8109.model.Book;
import ua.kpi.comsys.IV8109.retrieved_jsons.BooksJson;
import ua.kpi.comsys.IV8109.retrievers.RemoteJsonRetriever;
import ua.kpi.comsys.IV8109.storage.JSONHandler;
import ua.kpi.comsys.IV8109.storage.SQLiteDB;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BooksFragment extends Fragment {
    private static final String BOOK = "book";
    private static final String RESULT = "result";
    private static String booksJSON = "";

    ArrayList<Book> books = new ArrayList<>();
    BooksJson obtainedJSON = new BooksJson();
    ArrayList<Book> searchedList = new ArrayList<>();
    ListView list;
    BookAdapter adapter;
    ArrayList<String> textViewResourceId = new ArrayList<>();

    SQLiteDB hrcDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_books, container, false);

        TextView failedSearch = root.findViewById(R.id.failedSearch);
        failedSearch.setVisibility(View.VISIBLE);
        list = root.findViewById(R.id.BooksView);
        SearchView searchView = root.findViewById(R.id.searchView);

        hrcDB = new SQLiteDB(this.getContext());


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                if (adapter != null) {
                    adapter.clear();
                }

                if (queryText.length() >= 3) {
                    failedSearch.setVisibility(View.INVISIBLE);

                    /*---[ Preparing database ]---*/
                    // Connecting to db
                    SQLiteDatabase db = hrcDB.getWritableDatabase();

                    // Object for data pair - Book:JSON
                    ContentValues data = new ContentValues();

                    /*---[ Getting JSON from internet ]---*/
                    Gson gson = new Gson();
                    String urlRequest = String.format("https://api.itbook.store/1.0/search/%s", queryText);
                    RemoteJsonRetriever bookJsonRetriever = new RemoteJsonRetriever(urlRequest);
                    Thread thread = new Thread(bookJsonRetriever, "Request booklist JSON");
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /*---[ we shall have our JSON from internet at this point ]---*/

                    /* Handling JSON */
                    // [If JSON was obtained from internet -> store it in DB]
                    // [If it wasn't -> try to retrieve it from DB]
                    JSONHandler handler = new JSONHandler();
                    booksJSON = handler.handleJSON(queryText, booksJSON, db, "booktable");

                    // [If any source has the JSON  -> use it]
                    if (booksJSON != null) {
                        // working with JSON
                        obtainedJSON = gson.fromJson(booksJSON, BooksJson.class);

                        // forming list of book objects from BooksJson object
                        searchedList = obtainedJSON.getBooks();

                        // [if it has no results -> error message]
                        if (booksJSON.contains("\"total\":\"0\"")) {
                            failedSearch.setVisibility(View.VISIBLE);
                        }

                    // [If not -> error message]
                    } else {
                        searchedList.clear(); // clearing any leftovers
                        failedSearch.setVisibility(View.VISIBLE);
                    }

                    System.out.println("--List:" + searchedList);
                    updateResourceId(searchedList);

                    adapter = new BookAdapter(BooksFragment.this, searchedList, textViewResourceId);
                    list.setAdapter(adapter);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object listItem = list.getItemAtPosition(position);
                            openBookPage(id);
                        }
                    });

                } else {
                    failedSearch.setVisibility(View.VISIBLE);
                    // disconnecting from db
                    hrcDB.close();
                }

                return false;
            }
        });

        return root;
    }

    public static void setBooksJSON(String json) {
        booksJSON = json;
    }


    /* Place Book:JSON entry to DB */
    public void insertData(String book, String json, SQLiteDatabase db) {
        Cursor c = db.rawQuery("select book_request from booktable where book_request='" + book +"'", null);
        if (!c.moveToFirst()) {
            ContentValues data = new ContentValues();
            data.put("book_request", book);
            data.put("book_json", json);
            db.insert("booktable", null, data);
        }
//        } else {
//            c.close();
//        }
    }

    /* Get JSON from DB */
    public String getFromStorage(String book, SQLiteDatabase db) { ;
        String selection = "book_request='"+book+"'";
        Cursor c = db.query("booktable", null, selection, null, null, null, null);

        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndex("book_json"));
        }
//        } else {
//            c.close();
//        }

        return null;
    }


    public void refresh() {
        updateResourceId(searchedList);
        adapter = new BookAdapter(this, searchedList, textViewResourceId);
        list.setAdapter(adapter);
    }

    public void openBookPage(long id) {
        Intent intent = new Intent(this.getActivity(), ShowDesc.class);
        intent.putExtra(BOOK, searchedList.get((int) (id)).getisbn13());
        startActivity(intent);
    }

    private void updateResourceId (ArrayList<Book> list) {
        textViewResourceId.clear();
        for (Book book: list) {
            textViewResourceId.add(book.getTitle());
        }
    }

    private void updateJSON(String newData) {
        Gson gson = new Gson();
        Type bookType = new TypeToken<ArrayList<Book>>() {}.getType();
        books = gson.fromJson(newData, bookType);
        searchedList = books;
        refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String returnValue = data.getStringExtra(RESULT);
                updateJSON(returnValue);
            }
        }
    }
}

