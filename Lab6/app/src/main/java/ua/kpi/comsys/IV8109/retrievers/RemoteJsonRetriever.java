package ua.kpi.comsys.IV8109.retrievers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ua.kpi.comsys.IV8109.activities.ShowDesc;
import ua.kpi.comsys.IV8109.ui.books.BooksFragment;
import ua.kpi.comsys.IV8109.ui.gallery.GalleryFragment;

public class RemoteJsonRetriever implements Runnable {
    private String urlRequest;

    public RemoteJsonRetriever(String urlRequest) {
        this.urlRequest = urlRequest;
    }

    @Override
    public void run() {
        if (urlRequest.contains("https://api.itbook.store/1.0/search/")) {
            BooksFragment.setBooksJSON(obtainJSON());
        } else if (urlRequest.contains("https://api.itbook.store/1.0/books/")) {
            ShowDesc.setBookPageJSON(obtainJSON());
        } else if (urlRequest == "https://pixabay.com/api/?key=19193969-87191e5db266905fe8936d565&q=night+city&image_type=photo&per_page=27") {
            GalleryFragment.setGalleryJSON(obtainJSON());
        }


    }

    public String obtainJSON() {
        System.out.println("-------MyQuery (Book/BookDesc) " + urlRequest);

        URL url;
        try {
            url = new URL(urlRequest);

        HttpURLConnection connection = null;

        connection = (HttpURLConnection) url.openConnection();

        InputStream inputStream = connection.getInputStream();

        // converting inputStream to String (info and optimization advice from StackOverflow)

        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        // available
        StringBuilder total = new StringBuilder(inputStream.available());

        for (String line; (line = r.readLine()) != null;) {
            total.append(line);

        }
        connection.disconnect();

        String result = total.toString();
        return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
