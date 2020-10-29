package com.example.jbakingrecipe.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jbakingrecipe.R;
import com.example.jbakingrecipe.adapter.IngredientAdapter;
import com.example.jbakingrecipe.adapter.StepAdapter;
import com.example.jbakingrecipe.callback.StepCallback;
import com.example.jbakingrecipe.model.Ingredient;
import com.example.jbakingrecipe.model.Recipe;
import com.example.jbakingrecipe.model.Step;
import com.example.jbakingrecipe.viewmodel.DescriptionViewModel;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.Serializable;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity implements Serializable, StepCallback {

    private static final String TAG = "DescriptionActivity";
    TextView nameTextView;
    TextView servingsTextView;
    ToggleButton favoriteToggleButton;
    RecyclerView ingredientsRecyclerView;
    RecyclerView stepsRecyclerView;
    DescriptionViewModel descriptionViewModel;
    IngredientAdapter ingredientAdapter;
    StepAdapter stepAdapter;
    LinearLayoutManager linearLayoutManagerIng = new LinearLayoutManager(this);
    LinearLayoutManager linearLayoutManagerSte = new LinearLayoutManager(this);
    private SimpleExoPlayer simpleExoplayer;
    PlayerView playerView;
    Toast toast = null;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        initView();
    }

    void initView(){
        linearLayoutManagerIng.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManagerSte.setOrientation(LinearLayoutManager.VERTICAL);

        simpleExoplayer = new SimpleExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.playerView);

        nameTextView = findViewById(R.id.textViewName);
        servingsTextView = findViewById(R.id.textViewServings);
        favoriteToggleButton = findViewById(R.id.toggleButtonFavorite);
        ingredientsRecyclerView = findViewById(R.id.recyclerViewIngredients);
        stepsRecyclerView = findViewById(R.id.recyclerViewSteps);

        ingredientAdapter = new IngredientAdapter();
        stepAdapter = new StepAdapter();
        stepAdapter.setStepCallback(this);

        ingredientsRecyclerView.setLayoutManager(linearLayoutManagerIng);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        stepsRecyclerView.setLayoutManager(linearLayoutManagerSte);
        stepsRecyclerView.setAdapter(stepAdapter);

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
        String name = recipe.getName();
        String servings = recipe.getServings();
        String ingredients = recipe.getIngredients();
        String steps = recipe.getSteps();

        nameTextView.setText(name);
        servingsTextView.setText("Number of servings : " + servings);

        descriptionViewModel = new ViewModelProvider(this).get(DescriptionViewModel.class);
        descriptionViewModel.initDataIngredients(ingredients);
        descriptionViewModel.getMutableLiveDataIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                ingredientAdapter.setIngredientList(ingredients);
            }
        });

        descriptionViewModel.initDataSteps(steps);
        descriptionViewModel.getMutableLiveDataStep().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                Log.d(TAG, "onChanged: " + steps.get(0).getDescription());
                stepAdapter.setStepList(steps);
            }
        });

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        descriptionViewModel.checkFavorite(recipe);
        favoriteToggleButton.setChecked(descriptionViewModel.isFavorite);
        favoriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.startAnimation(scaleAnimation);
                if (isChecked == true) {
                    descriptionViewModel.insertFavorite(recipe);
                } else {
                    descriptionViewModel.deleteFavorite(recipe);
                }
            }
        });



    }

    @Override
    public void stepPressed(Step step) {
        simpleExoplayer.stop();
        if (step.getVideoURL().isEmpty()){

            playerView.setVisibility(View.VISIBLE);
            playerView.setPlayer(simpleExoplayer);
            playerView.setVisibility(View.GONE);

            if (toast != null) {
                toast = null;
            }
            toast = Toast.makeText(this, "Step " + step.getDescription(), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            playerView.setVisibility(View.VISIBLE);
            playerView.setPlayer(simpleExoplayer);
            playerView.setVisibility(View.VISIBLE);
            Uri videoUri = Uri.parse(step.getVideoURL());
            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            simpleExoplayer.setMediaItem(mediaItem);
            simpleExoplayer.prepare();
            simpleExoplayer.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer(simpleExoplayer);

    }

    @Override
    protected void onStop() {
        super.onStop();
        pausePlayer(simpleExoplayer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseExoPlayer(simpleExoplayer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlayer(simpleExoplayer);
    }

    public static void startPlayer(SimpleExoPlayer exoPlayer) {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    public static void pausePlayer(SimpleExoPlayer exoPlayer) {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    public static void releaseExoPlayer(SimpleExoPlayer exoPlayer) {
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        if (playerView.getVisibility() == View.VISIBLE){
            simpleExoplayer.stop();
            playerView.setVisibility(View.GONE);
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }
}