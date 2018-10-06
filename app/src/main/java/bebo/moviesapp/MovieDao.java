package bebo.moviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
   @Query("Select * From movie_table")
   LiveData< List<Movie>> loadAllMovies();

   @Query("Select * From movie_table WHERE id_movie = :id")
  LiveData< List<Movie>> loadById(int id);


    @Insert
   void insertMovie(Movie movie);

   @Query("Delete From movie_table WHERE id_movie = :favId")
   void deleteMovie(int favId);


}
