package com.example.samsophias.bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    public static final String RECIPE_JSON_STATE = "recipe_json_state";
    public static final String RECIPE_ARRAYLIST_STATE = "recipe_arraylist_state";

    RecipeInterface mRecipeInterface;
    MainAdapter mainAdapter;
    String mJsonResult;
    ArrayList<Recipe> mRecipeArrayList = new ArrayList<>();


    RecyclerView recyclerView;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView =findViewById(R.id.recycle_recipes);

        if(findViewById(R.id.recipe_tablet) != null){
            isTablet = true;
        }
        else{
            isTablet = false;
        }

        if(savedInstanceState != null){
            mJsonResult = savedInstanceState.getString(RECIPE_JSON_STATE);
            mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_ARRAYLIST_STATE);
            mainAdapter = new MainAdapter(MainActivity.this, mRecipeArrayList, mJsonResult);
            RecyclerView.LayoutManager mLayoutManager;
            if(isTablet){
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
            }
            else{
                mLayoutManager = new LinearLayoutManager(MainActivity.this);
            }

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mainAdapter);
        }
        else{
            if(MyNetUtility.isConnected(this)){
                mRecipeInterface = new MainClient().mRecipeInterface;
                new FetchRecipesAsync().execute();
            }
            else{
                MyDialUtility.showDialogWithButtons(this,
                        R.drawable.yellow_cake,
                        getResources().getString(R.string.no_internet_connection));
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_JSON_STATE, mJsonResult);
        outState.putParcelableArrayList(RECIPE_ARRAYLIST_STATE, mRecipeArrayList);
    }

    private void fetchRecipes() {
        Call<ArrayList<Recipe>> call = mRecipeInterface.getRecipes();

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                mRecipeArrayList = response.body();

                mJsonResult = new Gson().toJson(response.body());

                mainAdapter = new MainAdapter(MainActivity.this, mRecipeArrayList, mJsonResult);
                RecyclerView.LayoutManager mLayoutManager;
                if(isTablet){
                    mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                }
                else{
                    mLayoutManager = new LinearLayoutManager(MainActivity.this);
                }

                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mainAdapter);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }


    private class FetchRecipesAsync extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            fetchRecipes();
            return null;
        }
    }
}
