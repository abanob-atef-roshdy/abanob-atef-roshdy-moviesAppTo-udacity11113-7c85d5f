package bebo.moviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {
    Reviews reviews;
    List<Reviews> reviewsList;
    Context context;

    public ReviewsAdapter(List<Reviews> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int idOfReviewAdapter = R.layout.reviews_adapter;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        Boolean shouldAttatchFromParent = false;
        View view = layoutInflater.inflate(idOfReviewAdapter,viewGroup,shouldAttatchFromParent);
        ReviewHolder reviewHolder = new ReviewHolder(view);
        return  reviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
        reviews = reviewsList.get(i);
        reviewHolder.author_value.setText(reviews.getAuthor());
        reviewHolder.revDisp.setText(reviews.getContent());
    }



    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
   private TextView authorLabel,author_value,revDisp;
    public ReviewHolder(@NonNull View itemView) {
        super(itemView);
        authorLabel = itemView.findViewById(R.id.author_label_tv);
        author_value = itemView.findViewById(R.id.author_value_tv);
        revDisp = itemView.findViewById(R.id.review_disp_tv);
    }
}
public void swapReview(List<Reviews> list){
     reviewsList.addAll(list);
     notifyDataSetChanged();

}


}
