package com.example.samsophias.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecipeViewHolder>{

    private final Context mContext;
    private final ArrayList<Recipe> mRecipeList;
    private String mJsonResult;
    String recipeJson;

    public MainAdapter(Context context, ArrayList<Recipe> recipeList, String jsonResult) {
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mJsonResult = jsonResult;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {

        holder.recipeName.setText(mRecipeList.get(position).getName());

        switch (position){
            case 0 : holder.recipeIcon.setImageResource(R.drawable.nutella_pie);
                break;
            case 1 : holder.recipeIcon.setImageResource(R.drawable.brownie);
                break;
            case 2 : holder.recipeIcon.setImageResource(R.drawable.yellow_cake);
                break;
            case 3 : holder.recipeIcon.setImageResource(R.drawable.cheese_cake);
                break;
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
                recipeJson = jsonToString(mJsonResult, holder.getAdapterPosition());
                Intent intent = new Intent(mContext, MainDetailsActivity.class);
                ArrayList<Recipe> recipeArrayList = new ArrayList<>();
                recipeArrayList.add(recipe);
                intent.putParcelableArrayListExtra(MyConsUtility.RECIPE_INTENT_EXTRA, recipeArrayList);
                intent.putExtra(MyConsUtility.JSON_RESULT_EXTRA, recipeJson);
                mContext.startActivity(intent);

                SharedPreferences.Editor editor = mContext.getSharedPreferences(MyConsUtility.BAKINGAPP_SHARED_PREF, MODE_PRIVATE).edit();
                editor.putString(MyConsUtility.JSON_RESULT_EXTRA, recipeJson);
                editor.apply();

                if(Build.VERSION.SDK_INT > 25){
                    MyWidgetService.startActionOpenRecipeO(mContext);
                } else{
                    MyWidgetService.startActionOpenRecipe(mContext);
                }
            }
        });

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_card_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView recipeName;
        ImageView recipeIcon;

        public RecipeViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            recipeName = v.findViewById(R.id.recipe_name);
            recipeIcon = v.findViewById(R.id.icon_recipe);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    private String jsonToString(String jsonResult, int position){
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);
        return recipeElement.toString();
    }

}
