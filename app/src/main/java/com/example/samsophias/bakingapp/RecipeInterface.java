package com.example.samsophias.bakingapp;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

interface RecipeInterface {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
