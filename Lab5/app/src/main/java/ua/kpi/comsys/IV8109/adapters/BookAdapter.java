package ua.kpi.comsys.IV8109.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<String> {
    private final Fragment context;
    private final ArrayList<Book> books;
    List<String> textViewResourceId;

    public BookAdapter(Fragment context, ArrayList<Book> books, List<String> textViewResourceId) {

        super(context.getContext(), R.layout.fragment_book_item, textViewResourceId);

        this.context = context;
        this.books = books;
        this.textViewResourceId = textViewResourceId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.fragment_book_item, null,true);

        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        ImageView deleteButton = (ImageView) rowView.findViewById(R.id.deleteButton);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView priceText = (TextView) rowView.findViewById(R.id.price);

        System.out.println(position);

        int drawableResourceId = this.getContext().getResources().getIdentifier(
                books.get(position).getImage().toLowerCase().replace(".png", ""),
                "drawable", this.getContext().getPackageName());

        image.setImageResource(drawableResourceId);

        titleText.setText(books.get(position).getTitle());
        subtitleText.setText(books.get(position).getSubtitle());
        priceText.setText(books.get(position).getPrice());

        Bundle bundle = new Bundle();
        bundle.putString("delete", books.get(position).getTitle());

        deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(position);
            }
        });

        return rowView;
    };

    private void deleteBook(int position) {
        textViewResourceId.remove(position);
        books.remove(position);
        notifyDataSetChanged();
    }
}
