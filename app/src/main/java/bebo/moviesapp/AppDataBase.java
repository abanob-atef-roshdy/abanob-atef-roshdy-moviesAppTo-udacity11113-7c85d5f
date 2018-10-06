package bebo.moviesapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
@Database(entities = {Movie.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    private static final Object lock = new Object();
    private static final String dataBaseName = "moviedatabase";
    private static AppDataBase sInstance;

    public static AppDataBase getsInstance(Context context){

        if(sInstance == null){

            synchronized (lock){

                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,dataBaseName).build();
            }

        }

   return sInstance;

    }

public abstract MovieDao movieDao();

}
