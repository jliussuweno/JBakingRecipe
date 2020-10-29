package com.example.jbakingrecipe.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jbakingrecipe.R;
import com.example.jbakingrecipe.adapter.RecipeAdapter;
import com.example.jbakingrecipe.callback.RecipeCallback;
import com.example.jbakingrecipe.model.Recipe;
import com.example.jbakingrecipe.viewmodel.RecipeViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRecipeFragment extends Fragment {

    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    RecipeViewModel recipeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeAdapter = new RecipeAdapter();
        recipeAdapter.setRecipeCallback((RecipeCallback) getContext());

        recyclerView = view.findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(recipeAdapter);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.initRecipeData();
        recipeViewModel.getRecipeList().observe((LifecycleOwner) getContext(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                recipeAdapter.setRecipes(recipes);
            }
        });
    }
}