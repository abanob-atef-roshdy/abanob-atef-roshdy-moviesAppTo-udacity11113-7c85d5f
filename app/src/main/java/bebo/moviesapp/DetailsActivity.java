package bebo.moviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.ClickHandler {
    TextView originalTitleTv,overViewTv,voteTv,releaseTv;
    String filmTitle,fVote,fOverview,fRelease,fImgUrl;
    String Base_Url ;
    String trailerKey;
    ImageView imageView ;
    Movie movie2;
    TextView textView,trailer_tv,review_tv;
    String youtubeLink;
    List<Trailer> trailerList;
    List<Reviews> reviewsList;
    RecyclerView recyclerView;
    TrailerAdapter trailerAdapter;
    RecyclerView revRecView;
    ReviewsAdapter reviewsAdapter;
    AppDataBase mDb;
    ImageView star;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        originalTitleTv = findViewById(R.id.textView);
        overViewTv = findViewById(R.id.overview);
         voteTv = findViewById(R.id.vote);
         releaseTv = findViewById(R.id.release);
         trailer_tv = findViewById(R.id.trailerLabel_tv);
         review_tv = findViewById(R.id.revLabel_tv);
         imageView = findViewById(R.id.img);
         star = findViewById(R.id.starImgView);

         mDb = AppDataBase.getsInstance(getApplicationContext());


         recyclerView = findViewById(R.id.trailerRecView);
         LinearLayoutManager layoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);
         recyclerView.setHasFixedSize(true);
         revRecView = findViewById(R.id.rev_recView);
         LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
         revRecView.setLayoutManager(layoutManager1);
         revRecView.setHasFixedSize(true);

        Base_Url = "http://www.youtube.com/watch?v=";
        trailerList = new ArrayList<>();
        reviewsList = new ArrayList<>();
         populateUi();
        starColor();



        fetchJsonForTrailer();
        trailerAdapter = new TrailerAdapter(trailerList,getApplicationContext(),this);
        recyclerView.setAdapter(trailerAdapter);
       fetchJsonForReviews();
         reviewsAdapter = new ReviewsAdapter(reviewsList,getApplicationContext());
         revRecView.setAdapter(reviewsAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu,menu);
        return true;
    }
    public void populateUi(){
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movieObject");
        movie2 = movie;
        filmTitle = movie.getOriginal_title();
        originalTitleTv.setText(filmTitle);

        fImgUrl = movie.getPoster_path();
        Picasso.with(this).load(fImgUrl).into(imageView);

        fOverview = movie.getOverview();
        overViewTv.setText(fOverview);

        fVote = movie.getVote_average();
        voteTv.setText(fVote+"/10");

        fRelease = movie.getRelease_date();
        releaseTv.setText(fRelease);


    }
    public void starColor(){
        int id = movie2.getFilm_id();
        LiveData<List<Movie>> movie = mDb.movieDao().loadById(id);
        movie.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {

                if(movies.isEmpty()){

                    star.setImageResource(R.drawable.staricon);
                }
                else{

                    star.setImageResource(R.drawable.ystaricon);
                }



            }
        });


    }
    public void fetchJsonForTrailer(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int id = movie2.getFilm_id();
        String url = retUrlForTrailer(id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject all = new JSONObject(response);
                    JSONArray jsonArray = all.getJSONArray("results");
                    for(int i = 0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String videoKey = jsonObject.getString("key");
                        String videoName = jsonObject.getString("name");

                        Trailer trailer = new Trailer(videoName,videoKey);
                        trailerList.add(trailer);
                       }
                       } catch (JSONException e) {
                    e.printStackTrace();
                }
                trailerAdapter.swapData(trailerList);
                if(trailerList.isEmpty()){
                    trailer_tv.setText("no trailers available");
                }





            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    public String retUrlForTrailer(int id){
        String urlForTrailer = "https://api.themoviedb.org/3/movie/"+id+"/"+"videos"+"?api_key=";
       return urlForTrailer;
    }
    public String retUrlForٌReviews(int id){
        String urlForReviews = "https://api.themoviedb.org/3/movie/"+id+"/"+"reviews"+"?api_key=";
        return urlForReviews;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.showHome){
            onBackPressed();
        }
        if(id==R.id.settingsMenu){
            Intent goToSettingsIntent = new Intent(DetailsActivity.this,SettingsActivity.class);
            startActivity(goToSettingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTrailerClick(Trailer trailer) {
        trailerKey = trailer.getVideoKey();
        youtubeLink = Base_Url+trailerKey;
        Uri uri = Uri.parse(youtubeLink);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    public void fetchJsonForReviews() {
        RequestQueue queue = Volley.newRequestQueue(this);
        int id = movie2.getFilm_id();
        String urlForReviews = retUrlForٌReviews(id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlForReviews, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    try {
                        JSONObject all = new JSONObject(response);
                        JSONArray jsonArray = all.getJSONArray("results");
                        for(int i = 0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String author = jsonObject.getString("author");
                            String content = jsonObject.getString("content");
                            Reviews reviews = new Reviews(author,content);
                            reviewsList.add(reviews);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    reviewsAdapter.swapReview(reviewsList);
                    if(reviewsList.isEmpty()){
                        review_tv.setText("no reviews available");
                    }

                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }


    public void saveMovie() {

        int fId = movie2.getFilm_id();
    final Movie movie = new Movie(fVote,filmTitle,fImgUrl,fOverview,fRelease,fId);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                mDb.movieDao().insertMovie(movie);


            }
        });
    }
    public void deleteMovie1() {

        final int fId = movie2.getFilm_id();

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                mDb.movieDao().deleteMovie(fId);


            }
        });
    }



    public void storeOrDelete(View view) {
        int id = movie2.getFilm_id();



      AppDataBase mDb = AppDataBase.getsInstance(this);
      final LiveData<List<Movie>> movies1 = mDb.movieDao().loadById(id);
      movies1.observe(this, new Observer<List<Movie>>() {
          @Override
          public void onChanged(@Nullable List<Movie> movies) {
              if(movies.isEmpty()){
                   star.setImageResource(R.drawable.ystaricon);
                  saveMovie();

                  Toast.makeText(DetailsActivity.this, "movie was added to favourites list", Toast.LENGTH_SHORT).show();


              }
              else {
                  star.setImageResource(R.drawable.staricon);
                  deleteMovie1();

                  Toast.makeText(DetailsActivity.this, "movie was deleted from favourites list", Toast.LENGTH_SHORT).show();
                  // return;
              }
              movies1.removeObserver(this);


          }
      });


    }
}
