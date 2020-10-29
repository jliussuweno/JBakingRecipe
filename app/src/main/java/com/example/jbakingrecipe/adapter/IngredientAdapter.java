package com.example.jbakingrecipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jbakingrecipe.R;
import com.example.jbakingrecipe.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    List<Ingredient> ingredientList;

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tempView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(tempView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if (ingredientList != null){
            Ingredient current = ingredientList.get(position);
            holder.ingredientTextView.setText("- " + current.getName() + " : " + current.getQuantity() + " " + current.getMeasure());
        } else {
            holder.ingredientTextView.setText("No Ingredients");
        }
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null) {
            return ingredientList.size();
        } else {
            return 0;
        }

    }

    class IngredientViewHolder extends RecyclerView.ViewHolder{

        TextView ingredientTextView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientTextView = itemView.findViewById(R.id.textViewIngredient);
        }
    }
}
