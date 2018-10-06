package bebo.moviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>  {
    private List<Trailer> trailerList;
    Trailer trailer;
    private Context context;
    final private ClickHandler clickHandler;
    public interface ClickHandler{
        public void onTrailerClick(Trailer trailer);
    }

    public TrailerAdapter(List<Trailer> trailerList, Context context,ClickHandler clickHandler1) {
        this.trailerList = trailerList ;
        this.context = context;
        this.clickHandler = clickHandler1;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int idOfTrailerLayout = R.layout.trailer_adapter;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        Boolean shouldAttatchFromParent = false;
        View view = layoutInflater.inflate(idOfTrailerLayout,viewGroup,shouldAttatchFromParent);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
       trailer = trailerList.get(i);
      trailerViewHolder.textView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView textView;


    public TrailerViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.listTv);
        itemView.setOnClickListener(this);

    }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Trailer trailer = trailerList.get(position);
            clickHandler.onTrailerClick(trailer);
        }
    }
public void swapData(List<Trailer> list){

       trailerList.addAll(list);
        notifyDataSetChanged();

}

}
