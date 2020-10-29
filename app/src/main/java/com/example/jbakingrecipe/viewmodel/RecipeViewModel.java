package com.example.jbakingrecipe.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jbakingrecipe.adapter.RecipeAdapter;
import com.example.jbakingrecipe.database.RecipeDao;
import com.example.jbakingrecipe.database.RecipeRoomDatabase;
import com.example.jbakingrecipe.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecipeViewModel extends AndroidViewModel {

    Context context;
    MutableLiveData<List<Recipe>> recipeList;
    LiveData<List<Recipe>> listLiveData;
    RecipeRoomDatabase recipeRoomDatabase;
    RecipeDao mRecipeDao;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        recipeList = new MutableLiveData<>();
        recipeRoomDatabase = RecipeRoomDatabase.getDatabase(context);
        mRecipeDao = recipeRoomDatabase.recipeDao();
    }

    public MutableLiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

    public LiveData<List<Recipe>> getListLiveData() {
        return listLiveData;
    }

    public void initRecipeData(){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<Recipe> recipes = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String servings = jsonObject.getString("servings");
                                String ingredients = jsonObject.getString("ingredients");
                                String steps = jsonObject.getString("steps");
                                recipes.add(new Recipe(id, name, servings,ingredients, steps));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recipeList.postValue(recipes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: That didn't work!");
            }
        });

        queue.add(stringRequest);
    }

    public void selectFavorite(){
        listLiveData = mRecipeDao.selectRecipes();
    }
}
