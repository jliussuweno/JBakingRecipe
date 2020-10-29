package com.example.jbakingrecipe.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jbakingrecipe.database.RecipeDao;
import com.example.jbakingrecipe.database.RecipeRoomDatabase;
import com.example.jbakingrecipe.model.Ingredient;
import com.example.jbakingrecipe.model.Recipe;
import com.example.jbakingrecipe.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DescriptionViewModel extends AndroidViewModel {

    MutableLiveData<List<Ingredient>> mutableLiveDataIngredients;
    MutableLiveData<List<Step>> mutableLiveDataStep;
    LiveData<List<Recipe>> listLiveData;
    RecipeRoomDatabase recipeRoomDatabase;
    Context context;
    RecipeDao mRecipeDao;
    public Boolean isFavorite;

    public DescriptionViewModel(@NonNull Application application) {
        super(application);
        mutableLiveDataIngredients = new MutableLiveData<>();
        mutableLiveDataStep = new MutableLiveData<>();
        context = application.getApplicationContext();
        recipeRoomDatabase = RecipeRoomDatabase.getDatabase(context);
        mRecipeDao = recipeRoomDatabase.recipeDao();
        isFavorite = false;

    }

    public MutableLiveData<List<Ingredient>> getMutableLiveDataIngredients() {
        return mutableLiveDataIngredients;
    }

    public MutableLiveData<List<Step>> getMutableLiveDataStep() {
        return mutableLiveDataStep;
    }

    public void initDataIngredients(String ingredients){

        List<Ingredient> ingredient = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(ingredients);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String quantity = jsonObject.getString("quantity");
                String measure = jsonObject.getString("measure");
                String ingredientName = jsonObject.getString("ingredient");

                ingredient.add(new Ingredient(quantity,measure,ingredientName));
            }

            mutableLiveDataIngredients.postValue(ingredient);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void initDataSteps(String steps){

        List<Step> step = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(steps);

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");
                String shortDescription = jsonObject.getString("shortDescription");
                String description = jsonObject.getString("description");
                String videoURL = jsonObject.getString("videoURL");
                step.add(new Step(id,shortDescription,description,videoURL));
            }

            mutableLiveDataStep.postValue(step);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void insertFavorite(Recipe recipe){
        mRecipeDao.insertRecipe(recipe);
    }

    public void deleteFavorite(Recipe recipe){
        mRecipeDao.deleteRecipe(recipe.getId());
    }

    public void checkFavorite(Recipe recipe){
        if (mRecipeDao.checkRecipe(recipe.getId()) == 1){
            isFavorite = true;
        } else {
            isFavorite = false;
        }
    }

}
