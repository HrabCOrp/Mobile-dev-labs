package ua.kpi.comsys.IV8109.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.arasthel.spannedgridlayoutmanager.SpanSize;
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.jvm.functions.Function1;
import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.adapters.GalleryAdapter;


public class GalleryFragment extends Fragment {
    public ArrayList<Bitmap> imageList = new ArrayList<>();
    public GalleryAdapter galleryAdapter = new GalleryAdapter(this, imageList);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.setRetainInstance(true);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.galleryGrid);
        recyclerView.setNestedScrollingEnabled(false);

        SpannedGridLayoutManager spannedGridLayoutManager = new SpannedGridLayoutManager(
                SpannedGridLayoutManager.Orientation.VERTICAL, 3);
        spannedGridLayoutManager.setItemOrderIsStable(false);

        spannedGridLayoutManager.setSpanSizeLookup(new SpannedGridLayoutManager.SpanSizeLookup(new Function1<Integer, SpanSize>(){
            @Override public SpanSize invoke(Integer position) {
                if (position == 4) {
                    return new SpanSize(2, 2);
                } else if ((position - 4) % 9 == 0) {
                    return new SpanSize(2, 2);
                } else {
                    return new SpanSize(1, 1);
                }
            }
        }));

        Button addImage = root.findViewById(R.id.addImage);

        recyclerView.setLayoutManager(spannedGridLayoutManager);
        recyclerView.setAdapter(galleryAdapter);

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(
                            getContext().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap scaled = selectedImage.createScaledBitmap(selectedImage,
                        (int)Math.ceil(selectedImage.getWidth()/2), (int)Math.ceil(selectedImage.getHeight()/2), false);
                imageList.add(scaled);
                galleryAdapter.notifyDataSetChanged();
            }
        }
    }
}