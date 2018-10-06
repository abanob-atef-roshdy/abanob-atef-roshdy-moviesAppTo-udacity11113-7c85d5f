package bebo.moviesapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
@Entity(tableName = "movie_table")
public class Movie implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @ColumnInfo(name = "vote_table")
    String vote_average;
    @ColumnInfo(name = "title_table")
    String original_title;
    @ColumnInfo(name = "image_table")
    String poster_path;
    @ColumnInfo(name = "overview_table")
    String overview;
    @ColumnInfo(name = "release_table")
    String release_date;
    @ColumnInfo(name = "id_movie")
    int film_id;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_database")
    int id;
     @Ignore
    public Movie(String vote_average, String original_title, String poster_path, String overview, String release_date,int film_id) {
        this.vote_average = vote_average;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.film_id = film_id;


    }

    public Movie(  int id,String vote_average, String original_title, String poster_path, String overview, String release_date, int film_id) {
        this.id = id;
        this.vote_average = vote_average;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.film_id = film_id;

    }

    public String getVote_average() {
        return vote_average;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getFilm_id() {
        return film_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.vote_average);
        parcel.writeString(this.original_title);
        parcel.writeString(this.poster_path);
        parcel.writeString(this.overview);
        parcel.writeString(this.release_date);
        parcel.writeInt(this.film_id);

    }

    public Movie(Parcel in) {
        this.vote_average = in.readString();
        this.original_title = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.film_id = in.readInt();

    }
}
