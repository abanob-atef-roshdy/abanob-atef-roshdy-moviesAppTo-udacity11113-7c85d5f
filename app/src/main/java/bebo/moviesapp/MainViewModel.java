package bebo.moviesapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> movie;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDataBase mDb = AppDataBase.getsInstance(this.getApplication());
        movie = mDb.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovie() {
        return movie;
    }
}
