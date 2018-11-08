package com.example.samsophias.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;

public class MainDetailsAdapter extends RecyclerView.Adapter<MainDetailsAdapter.RecipeDetailsViewHolder>{

    private final Context mContext;
    private final List<Ingredient> mIngredientList;

    public MainDetailsAdapter(Context context, List<Ingredient> ingredientList) {
        this.mContext = context;
        this.mIngredientList = ingredientList;
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    public class RecipeDetailsViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView textView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageView imageView1;

        public RecipeDetailsViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            imageView = v.findViewById(R.id.unit_icon);
            textView = v.findViewById(R.id.ingredient_name);
            textView1 = v.findViewById(R.id.unit_number);
            textView2 = v.findViewById(R.id.ingredient_number);
            textView3 = v.findViewById(R.id.unit_name);
            imageView1 = v.findViewById(R.id.ingredient_checked);


        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecipeDetailsViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);

        holder.textView.setText(ingredient.getIngredient());
        holder.textView1.setText(String.valueOf(ingredient.getQuantity()));
        holder.textView2.setText(String.valueOf(position+1));

        String measure = ingredient.getMeasure();
        int unitNo = 0;

        for(int i = 0; i < MyConsUtility.units.length; i++){
            if(measure.equals(MyConsUtility.units[i])){
                unitNo = i;
                break;
            }
        }
        int unitIcon = MyConsUtility.unitIcons[unitNo];
        Log.d("UNIT_NO: ", String.valueOf(unitIcon));
        String unitLongName = MyConsUtility.unitName[unitNo];

        holder.imageView.setImageResource(unitIcon);
        holder.textView3.setText(unitLongName);

        final boolean isChecked = false;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.imageView1.getVisibility() == View.GONE){
                    holder.imageView1.setVisibility(View.VISIBLE);
                } else{
                    holder.imageView1.setVisibility(View.GONE);
                }


            }
        });

    }

    @NonNull
    @Override
    public RecipeDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardview_ingredients, parent, false);

        return new RecipeDetailsViewHolder(view);
    }
}