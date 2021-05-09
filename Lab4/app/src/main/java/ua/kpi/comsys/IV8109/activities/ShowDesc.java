package ua.kpi.comsys.IV8109.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.model.BookPage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class ShowDesc extends Activity {
    private static final String BOOK = "book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_desc);
        Intent intent = getIntent();
        String book = intent.getStringExtra(BOOK);

        String fileName = book + ".json";
        Gson gson = new Gson();

        BookPage description = new BookPage();
        Type bookPageType = new TypeToken<BookPage>() {}.getType();
        try {
            description = gson.fromJson(ReadTextFile(fileName), bookPageType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.description);
        tv.setText(description.toString());
        int drawableResourceId = 0;
        if (description.getImage() != null){
            drawableResourceId = this.getResources().getIdentifier(
                    description.getImage().toLowerCase().replace(".png", ""),
                    "drawable", this.getPackageName());
            System.out.println(description.getImage().toLowerCase());
        }
        ImageView image = findViewById(R.id.image_desc);
        System.out.println("Image:" + drawableResourceId);
        image.setImageResource(drawableResourceId);
    }

    public String ReadTextFile(String name) throws IOException {
        StringBuilder string = new StringBuilder();
        String line = "";
        InputStream is = this.getAssets().open(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            string.append(line);
        }
        is.close();
        return string.toString();
    }
}