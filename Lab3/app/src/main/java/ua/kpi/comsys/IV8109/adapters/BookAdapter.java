package ua.kpi.comsys.IV8109.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.model.Book;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<String> {

    private final Fragment context;
    private final ArrayList<Book> books;
    private final ArrayList<String> title;

    public BookAdapter(Fragment context, ArrayList<Book> books, ArrayList<String> title) {

        super(context.getContext(), R.layout.fragment_book_item, title);

        this.context = context;
        this.books = books;
        this.title = title;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.fragment_book_item, null,true);

        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView priceText = (TextView) rowView.findViewById(R.id.price);

        System.out.println(position);

        int drawableResourceId = this.getContext().getResources().getIdentifier(
                books.get(position).getImage().toLowerCase().replace(".png", ""),
                "drawable", this.getContext().getPackageName());

        image.setImageResource(drawableResourceId);

        titleText.setText(title.get(position));
        subtitleText.setText(books.get(position).getSubtitle());
        priceText.setText(books.get(position).getPrice());

        return rowView;
    };
}
