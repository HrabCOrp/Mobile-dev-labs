package ua.kpi.comsys.IV8109.ui.books;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.activities.AddBook;
import ua.kpi.comsys.IV8109.activities.ShowDesc;
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
    private static final String BOOK = "book";
    private static final String RESULT = "result";
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Book> searchedList = new ArrayList<>();
    ListView list;
    BookAdapter adapter;
    ArrayList<String> textViewResourceId = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_books, container, false);

        String fileName = "Books.json";

        TextView failedSearch = root.findViewById(R.id.failedSearch);
        failedSearch.setVisibility(View.INVISIBLE);

        Gson gson = new Gson();
        Type bookType = new TypeToken<ArrayList<Book>>() {}.getType();

        try {
            books = gson.fromJson(ReadJson(fileName), bookType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchedList = books;
        updateResourceId(searchedList);

        list = root.findViewById(R.id.BooksView);
        adapter = new BookAdapter(this, searchedList, textViewResourceId);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = list.getItemAtPosition(position);
                openBookPage(id);
            }
        });

        SearchView searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchedList =  filter(newText);
                if (newText.equals(""))
                    searchedList = books;
                if (searchedList.size() > 0) {
                    failedSearch.setVisibility(View.INVISIBLE);
                } else {
                    failedSearch.setVisibility(View.VISIBLE);
                }
                refresh();

                return false;
            }
        });

        ImageView addItemButton = (ImageView) root.findViewById(R.id.addItem);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    openAddBookActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
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

    public String ReadJson(String filename) throws IOException {
        StringBuilder string = new StringBuilder();
        String line = "";
        try {
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
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return string.toString();
    }

    public ArrayList<Book> filter(String searchText) {
        ArrayList<Book> newList = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    book.getSubtitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    book.getPrice().contains(searchText)) {
                newList.add(book);
            }
        }

        return newList;
    }

    private void updateResourceId (ArrayList<Book> list) {
        textViewResourceId.clear();
        for (Book book: list) {
            textViewResourceId.add(book.getTitle());
        }
    }

    public void openAddBookActivity() throws IOException {
        Intent intent = new Intent(this.getActivity(), AddBook.class);
        intent.putExtra(BOOK, booksToString());
        startActivityForResult(intent, 1);
    }

    private void updateJSON(String newData) {
        Gson gson = new Gson();
        Type listOfMoviesItemsType = new TypeToken<ArrayList<Book>>() {}.getType();
        books = gson.fromJson(newData, listOfMoviesItemsType);
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

    private String booksToString() {
        StringBuilder result = new StringBuilder("[ ");
        for (int i = 0; i < books.size(); i++) {
            if (i < books.size() - 1) {
                result.append(books.get(i).toString());
                result.append(", ");
            }
            else result.append(books.get(i).toString());

        }
        return result.append(" ]").toString();
    }
}