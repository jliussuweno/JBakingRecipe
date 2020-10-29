package com.example.jbakingrecipe.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.jbakingrecipe.model.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeRoomDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();
    public static RecipeRoomDatabase INSTANCE;

    public static RecipeRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (RecipeRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeRoomDatabase.class, "recipe_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };


}
