package com.example.jbakingrecipe.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.jbakingrecipe.R;
import com.example.jbakingrecipe.adapter.RecipeAdapter;
import com.example.jbakingrecipe.callback.RecipeCallback;
import com.example.jbakingrecipe.model.Recipe;
import com.example.jbakingrecipe.viewmodel.RecipeViewModel;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeCallback, Serializable {

    TabLayout tabLayout;
    ViewPager viewPager;
    CustomPagerAdapter customPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),2);
        viewPager.setAdapter(customPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void recipePressed(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
        intent.putExtra("recipe", (Serializable) recipe);
        startActivity(intent);
    }
}