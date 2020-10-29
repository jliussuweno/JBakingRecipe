package com.example.jbakingrecipe.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jbakingrecipe.R;
import com.example.jbakingrecipe.callback.StepCallback;
import com.example.jbakingrecipe.model.Step;

import java.util.List;

import static android.content.ContentValues.TAG;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    List<Step> stepList;
    StepCallback stepCallback = null;

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
        notifyDataSetChanged();
    }

    public void setStepCallback(StepCallback stepCallback) {
        this.stepCallback = stepCallback;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View tempView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(tempView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        if (stepList != null){
            Step current = stepList.get(position);
            holder.stepName.setText(current.getId() + " " + current.getShortDescription());
            Log.d(TAG, "onBindViewHolder: " + current.getShortDescription());
            holder.getParent().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stepCallback != null){
                        stepCallback.stepPressed(current);
                    }
                }
            });
        } else {
            holder.stepName.setText("No Steps");
        }
    }

    @Override
    public int getItemCount() {
        if (stepList != null){
            return stepList.size();
        } else {
            return 0;
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        TextView stepName;
        View parent;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            stepName = itemView.findViewById(R.id.textViewStepName);
        }

        public View getParent() {
            return parent;
        }
    }
}
