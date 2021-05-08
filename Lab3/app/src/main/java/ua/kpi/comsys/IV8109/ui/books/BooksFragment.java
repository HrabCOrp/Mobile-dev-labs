package ua.kpi.comsys.IV8109.ui.books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.adapters.BookAdapter;
import ua.kpi.comsys.IV8109.model.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BooksFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_books, container, false);

        String fileName = "Books.json";

        Gson gson = new Gson();
        Type bookType = new TypeToken<ArrayList<Book>>() {}.getType();
        ArrayList<Book> bookList= new ArrayList<>();
        ArrayList<String> bookTitle = new ArrayList<>();

        try {
            bookList = gson.fromJson(ReadJson(fileName), bookType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Book book: bookList) {
            bookTitle.add(book.getTitle());
        }
        
        ListView list = root.findViewById(R.id.BooksView);
        BookAdapter adapterBook = new BookAdapter(this, bookList, bookTitle);
        list.setAdapter(adapterBook);

        return root;
    }

    public String ReadJson(String filename) throws IOException {
        StringBuilder string = new StringBuilder();
        String line = "";
        InputStream input = getContext().getAssets().open(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            try {
                if ((line = reader.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            string.append(line);
        }
        input.close();
        return string.toString();
    }
}