package ua.kpi.comsys.IV8109.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.model.BookPage;
import ua.kpi.comsys.IV8109.retrievers.RemoteJsonRetriever;
import ua.kpi.comsys.IV8109.storage.JSONHandler;
import ua.kpi.comsys.IV8109.storage.SQLiteDB;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShowDesc extends Activity {
    private static final String BOOK = "book";
    private static String bookPageJSON;
    SQLiteDB hrcDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_desc);
        Intent intent = getIntent();
        String book = intent.getStringExtra(BOOK);

        hrcDB = new SQLiteDB(this);

        /*---[ Preparing database ]---*/
        // Connecting to db
        SQLiteDatabase db = hrcDB.getWritableDatabase();

        // Object for data pair - Bookpage:JSON
        ContentValues data = new ContentValues();

        /*---[ Getting JSON from internet ]---*/
        Gson gson = new Gson();
        String urlRequest = String.format("https://api.itbook.store/1.0/books/%s", book);
        RemoteJsonRetriever bookJsonRetriever = new RemoteJsonRetriever(urlRequest);
        Thread thread = new Thread(bookJsonRetriever, "Request bookPage JSON");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*---[ we shall have our JSON from internet at this point ]---*/


        /* Handling JSON situation */
        JSONHandler handler = new JSONHandler();
        bookPageJSON = handler.handleJSON(book, bookPageJSON, db, "bookpagetable");

        System.out.println(">>>>>>BookPageJSON" + bookPageJSON);

        if (bookPageJSON != null) {
            BookPage description = new BookPage();
            Type bookPageType = new TypeToken<BookPage>() {
            }.getType();

            description = gson.fromJson(bookPageJSON, bookPageType);
            TextView tv = (TextView) findViewById(R.id.description);
            TextView failedDesc = (TextView) findViewById(R.id.failedDesc);
            tv.setText(description.toString());

            if (description.getImage() != null) {
                failedDesc.setVisibility(View.INVISIBLE);
                ImageView image = findViewById(R.id.image_desc);
                Glide.with(this).load(description.getImage()).into(image);
            } else {
                failedDesc.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void setBookPageJSON(String response) {
        bookPageJSON = response;
    }

}