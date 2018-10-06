package bebo.moviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<Movie> movieList;
    private  Context context;
   final   private ImageClickHandler imageClickHandler;
    public interface ImageClickHandler{
        public void onImageClick(Movie movie);
    }



    public ImageAdapter(List<Movie> movieList, Context context, ImageClickHandler clickHandler) {
        this.movieList = new ArrayList<>( movieList);
        this.context = context;
         this.imageClickHandler = clickHandler;


    }
    public void updateData(List<Movie> movies) {
        movieList.clear();
        movieList.addAll(movies);
        notifyDataSetChanged();
    }
    public void clear() {
        final int size = movieList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                movieList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int idOfImageAdapter = R.layout.image_adapter;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        Boolean shouldAttatchFromParent = false;
        View view = layoutInflater.inflate(idOfImageAdapter,viewGroup,shouldAttatchFromParent);
         ImageViewHolder imageViewHolder = new ImageViewHolder(view);
         return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Movie movie = movieList.get(i);
        Picasso.with(context).load(movie.getPoster_path()).into(imageViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = movieList.get(position);
            imageClickHandler.onImageClick(movie);


        }
    }
}
