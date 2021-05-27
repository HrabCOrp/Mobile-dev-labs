package ua.kpi.comsys.IV8109.retrieved_jsons;

import java.util.ArrayList;

import ua.kpi.comsys.IV8109.model.Image;

public class GalleryJson {
    String total;
    String totalHits;
    ArrayList<Image> hits;

    public ArrayList<Image> getImage() {
        return hits;
    }

}
