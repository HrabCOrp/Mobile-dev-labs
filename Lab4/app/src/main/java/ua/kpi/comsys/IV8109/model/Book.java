package ua.kpi.comsys.IV8109.model;

public class Book {
    private String title;
    private String subtitle;
    private String isbn13;
    private String price;
    private String image;

       public String getTitle() { return title; }

    public String getSubtitle() { return subtitle; }

    public String getisbn13() { return isbn13; }

    public String getPrice() { return price; }

    public String getImage() { return image; }

    public String toString() {
        return "{\"title\":\"" + title + "\"," +
                "\"subtitle\":\"" + subtitle + "\"," +
                "\"isbn13\": \""+ isbn13 + "\"," +
                "\"price\":\"" + price + "\"," +
                "\"image\":\"" + image + "\"}";
    }

}
