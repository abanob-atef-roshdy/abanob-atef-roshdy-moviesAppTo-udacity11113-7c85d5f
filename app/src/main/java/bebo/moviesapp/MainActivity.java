package bebo.moviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ImageClickHandler,SharedPreferences.OnSharedPreferenceChangeListener {


    int flag;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.settingsMenu){
            Intent goToSettingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(goToSettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    List<Movie> movieList;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    AppDataBase mDb;
    String url;
    Parcelable listState;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recView);
        gridLayoutManager = new GridLayoutManager(this,calculateNoOfColumns(this));
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(savedInstanceState != null){
            listState = savedInstanceState.getParcelable("f");
            gridLayoutManager.onRestoreInstanceState(listState);
            movieList = savedInstanceState.getParcelableArrayList("list");
            flag = savedInstanceState.getInt("fav");
            if(flag == 1)
            {
                retFromDb();
            }

        }
        else {


            loadData(sharedPreferences);
        }
        imageAdapter = new ImageAdapter(movieList,getApplicationContext(), this);
        recyclerView.setAdapter(imageAdapter);
        mDb = AppDataBase.getsInstance(getApplicationContext());
      // retFromDb();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable("f",listState);
        outState.putSerializable("list", (Serializable) movieList);
        outState.putInt("fav", flag);
    }

 /*   @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
           listState= savedInstanceState.getParcelable("f");
        }
    }*/

    public  int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
    public void loadData(SharedPreferences sharedPreferences){

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        movieList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, retUrl(sharedPreferences), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject ob1 = new JSONObject(response);
                    JSONArray a1 = ob1.getJSONArray("results");
                    for(int i = 0 ; i<a1.length();i++) {
                        JSONObject ob = a1.getJSONObject(i);
                        String baseUrl = "http://image.tmdb.org/t/p/w342/";
                        String image =  baseUrl + ob.getString("poster_path");

                        String Title = ob.getString("title");
                        Double vote1 = ob.getDouble("vote_average");
                        String vote = vote1.toString();
                        String releaseDate = ob.getString("release_date");
                        String overview = ob.getString("overview");
                        int id = ob.getInt("id");
                        Movie movie = new Movie(vote,Title,image,overview,releaseDate,id);
                        movieList.add(movie);
                    }

                    } catch (JSONException e) {
                    e.printStackTrace();
                }
          
                imageAdapter.updateData(movieList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onImageClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this,DetailsActivity.class).putExtra("movieObject",movie);

                startActivity(intent);


    }
    public String retUrl(SharedPreferences sharedPreferences){

        String sort = sharedPreferences.getString(getString(R.string.listKey),getString(R.string.listDefValue));
        if(sort.equals("popular")) {
            flag = 0;
             url = "https://api.themoviedb.org/3/movie/popular?api_key=9210a90684bb5c798a69dcc6f9ba6889";
        }
        else if (sort.equals("topRated")){
            flag = 0;
             url = "https://api.themoviedb.org/3/movie/top_rated?api_key=9210a90684bb5c798a69dcc6f9ba6889";
        }
        else {
            url = "";
            flag = 1;
            retFromDb();
        }

return url;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String pref = sharedPreferences.getString(getString(R.string.listKey),getString(R.string.listDefValue));
        if(pref.equals("favourite")){
            retFromDb();
        }
        else {
            loadData(sharedPreferences);
        }
    }
    public void retFromDb(){
        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

      viewModel.getMovie().observe(this, new Observer<List<Movie>>() {
          @Override
          public void onChanged(@Nullable List<Movie> movies) {
              imageAdapter.updateData(movies);


          }

      });


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = sharedPreferences.getString(getString(R.string.listKey),getString(R.string.listDefValue));
        if(pref.equals("favourite")){
            retFromDb();
        }
        else {
            loadData(sharedPreferences);
        }

        if(listState != null){

            gridLayoutManager.onRestoreInstanceState(listState);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onPause() {
        super.onPause();


    }
}
