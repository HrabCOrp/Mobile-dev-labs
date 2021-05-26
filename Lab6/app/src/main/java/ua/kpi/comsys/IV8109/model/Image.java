package ua.kpi.comsys.IV8109.model;

import android.graphics.Bitmap;

public class Image {
    // custom field
    private Bitmap bitmap = null;

    // json keys
    private String id;
    private String pageURL;
    private String type;
    private String tags;
    private String previewURL;
    private String previewWidth;
    private String previewHeight;
    private String webformatURL;
    private String webformatWidth;
    private String webformatHeight;
    private String largeImageURL;
    private String imageWidth;
    private String imageHeight;
    private String imageSize;
    private String views;
    private String downloads;
    private String favorites;
    private String likes;
    private String comments;
    private String user_id;
    private String user;
    private String userImageURL;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public String getWebformatURL() {
        return webformatURL;
    }

}