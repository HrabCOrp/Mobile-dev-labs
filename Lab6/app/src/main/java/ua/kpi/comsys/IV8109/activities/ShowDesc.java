package ua.kpi.comsys.IV8109.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.model.BookPage;
import ua.kpi.comsys.IV8109.retrievers.RemoteJsonRetriever;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ShowDesc extends Activity {
    private static final String BOOK = "book";
    private static String bookPageJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_desc);
        Intent intent = getIntent();
        String book = intent.getStringExtra(BOOK);


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
        System.out.println(">>>>>>BookPageJSON"+bookPageJSON);

        BookPage description = new BookPage();
        Type bookPageType = new TypeToken<BookPage>(){}.getType();

        description = gson.fromJson(bookPageJSON, bookPageType);

        TextView tv = (TextView) findViewById(R.id.description);
        tv.setText(description.toString());

        if (description.getImage() != null) {
            ImageView image = findViewById(R.id.image_desc);
            Glide.with(this).load(description.getImage()).into(image);
        }
    }

    public static void setBookPageJSON(String response) {
        bookPageJSON = response;
    }

}