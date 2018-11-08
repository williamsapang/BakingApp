package com.example.samsophias.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_LIST_STATE = "recipe_details_list";
    public static final String RECIPE_JSON_STATE = "recipe_json_list";


    RecyclerView mRecyclerView;


    Button mButtonStartCooking;

    MainDetailsAdapter mMainDetailsAdapter;
    ArrayList<Recipe> mRecipeArrayList;
    ArrayList<Step> mStepArrayList;
    String mJsonResult;
    List<Ingredient> mIngredientList;
    private boolean isTablet;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, mRecipeArrayList);
        outState.putString(RECIPE_JSON_STATE, mJsonResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_details);
        ButterKnife.bind(this);
        mRecyclerView = findViewById(R.id.recycle_ingredients);
        mButtonStartCooking = findViewById(R.id.button_start);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(findViewById(R.id.recipe_details_tablet) != null){
            isTablet = true;
        } else{
            isTablet = false;
        }


        if(getIntent().getStringExtra(MyConsUtility.WIDGET_EXTRA) != null){
            SharedPreferences sharedpreferences =
                    getSharedPreferences(MyConsUtility.BAKINGAPP_SHARED_PREF,MODE_PRIVATE);
            String jsonRecipe = sharedpreferences.getString(MyConsUtility.JSON_RESULT_EXTRA, "");
            mJsonResult = jsonRecipe;

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

            mStepArrayList = (ArrayList<Step>) recipe.getSteps();
            mIngredientList = recipe.getIngredients();
        } else{
            if(savedInstanceState != null)
            {
                mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);
                mJsonResult = savedInstanceState.getString(RECIPE_JSON_STATE);
                mStepArrayList = (ArrayList<Step>) mRecipeArrayList.get(0).getSteps();
                mIngredientList = mRecipeArrayList.get(0).getIngredients();
            } else{
                Intent recipeIntent = getIntent();
                mRecipeArrayList = recipeIntent.getParcelableArrayListExtra(MyConsUtility.RECIPE_INTENT_EXTRA);
                mJsonResult = recipeIntent.getStringExtra(MyConsUtility.JSON_RESULT_EXTRA);
                mStepArrayList = (ArrayList<Step>) mRecipeArrayList.get(0).getSteps();
                mIngredientList = mRecipeArrayList.get(0).getIngredients();
            }
        }

        mMainDetailsAdapter = new MainDetailsAdapter(this, mIngredientList);
        RecyclerView.LayoutManager mLayoutManager;
        if(isTablet){
            mLayoutManager = new GridLayoutManager(this, 2);
        } else{
            mLayoutManager = new LinearLayoutManager(this);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMainDetailsAdapter);
        mButtonStartCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDetailsActivity.this, BrewActivity.class);
                intent.putParcelableArrayListExtra(MyConsUtility.STEP_INTENT_EXTRA, mStepArrayList);
                intent.putExtra(MyConsUtility.JSON_RESULT_EXTRA, mJsonResult);
                startActivity(intent);
            }
        });
    }
}