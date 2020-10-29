package com.example.jbakingrecipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jbakingrecipe.R;
import com.example.jbakingrecipe.callback.RecipeCallback;
import com.example.jbakingrecipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    List<Recipe> recipes = new ArrayList<>();
    RecipeCallback recipeCallback = null;

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void setRecipeCallback(RecipeCallback recipeCallback){
        this.recipeCallback = recipeCallback;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        if (recipes != null){
            Recipe current = recipes.get(position);
            holder.nameTextView.setText(current.getName());
            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recipeCallback != null){
                        recipeCallback.recipePressed(current);
                    }
                }
            });
        } else {
            holder.nameTextView.setText("No Recipes");
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final View parent;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView);
            parent = itemView;
        }

        private View getView(){
            return parent;
        }

    }
}
