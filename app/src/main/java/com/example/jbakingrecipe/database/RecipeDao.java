package com.example.jbakingrecipe.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jbakingrecipe.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insertRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes_table")
    LiveData<List<Recipe>> selectRecipes();

    @Query("DELETE FROM recipes_table where id = :id")
    void deleteRecipe(String id);

    @Query("SELECT COUNT(*) FROM recipes_table where id= :id")
    int checkRecipe(String id);
}
