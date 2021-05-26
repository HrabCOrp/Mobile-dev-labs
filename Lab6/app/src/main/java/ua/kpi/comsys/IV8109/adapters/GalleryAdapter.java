package ua.kpi.comsys.IV8109.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import ua.kpi.comsys.IV8109.R;
import ua.kpi.comsys.IV8109.model.Image;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private final Fragment fragment;
    private final ArrayList<Image> imageList;

    public GalleryAdapter(Fragment fragment, ArrayList<Image> imageList) {
        this.fragment = fragment;
        this.imageList = imageList;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ProgressBar progressBar;
        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.galleryImage);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.progressBar.setIndeterminate(true);
            this.progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image, parent, false);

        return new GalleryAdapter.GalleryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.GalleryViewHolder holder, int position) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> imageList: " + this.imageList.get(0).getWebformatURL());
        if (imageList.get(position).getBitmap() != null) {
            holder.progressBar.setVisibility(View.INVISIBLE);
            System.out.println("<<<< Has bitmap >>>>");
            holder.imageView.setImageBitmap(imageList.get(position).getBitmap());
        } else {
            System.out.println("<<<< Has url >>>>");

            Glide.with(holder.imageView)
                    .load(imageList.get(position).getWebformatURL())
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })


                    .into(holder.imageView);

            //Glide.with(holder.imageView).load(imageList.get(position).getWebformatURL()).into(holder.imageView);
        }

    }
    
    @Override
    public int getItemCount() {
        return imageList.size();
    }

}