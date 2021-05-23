package ua.kpi.comsys.IV8109.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ua.kpi.comsys.IV8109.R;

public class AddBook extends Activity {
    private static final String fileName = "Books.json";
    private static final String BOOK = "book";
    private static final String RESULT = "result";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Intent intent = getIntent();
        String filetext = intent.getStringExtra(BOOK);
        TextView title = (TextView) findViewById(R.id.titleText);
        TextView subtitle = (TextView) findViewById(R.id.subtitleText);
        TextView price = (TextView) findViewById(R.id.priceText);

        Button button = findViewById(R.id.addMovieButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newMovie = "";
                newMovie = filetext.replace("]", "");
                newMovie += ", {\"title\":\"" + title.getText().toString() + "\"," +
                        "\"subtitle\":\"" + subtitle.getText().toString() + "\"," +
                        "\"isbn13\":\"\"," +
                        "\"price\":\"$" + price.getText().toString() + "\"," +
                        "\"image\":\"\"} ]";

                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT, newMovie);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}