package com.example.samsophias.bakingapp;

public class MyConsUtility {

    public static final String JSON_RESULT_EXTRA = "json_result_extra";
    public static final String WIDGET_EXTRA = "widget_extra";
    public static final String RECIPE_INTENT_EXTRA = "recipe_intent_extra";
    public static final String STEP_SINGLE = "step_single";
    public static final String STEP_ARRAYLIST = "step_arraylist";
    public static final String STEP_INTENT_EXTRA = "step_intent_extra";
    public static final String STEP_VIDEO_URI = "step_video_uri";
    public static final String STEP_THUMBNAIL_URI = "step_thumbnail_uri";
    public static final String STEP_DESCRIPTION = "step_description";
    public static final String STEP_SHORT_DESCRIPTION = "step_short_description";
    public static final String STEP_NUMBER = "step_number";
    public static final String BAKINGAPP_SHARED_PREF = "bakingapp_shared_pref";

    public static String [] units = {"CUP","TBLSP","TSP","G","K","OZ","UNIT"};
    public static String [] unitName = {"Cup","Tablespoon","Teaspoon","Gram","Kilogram","Ounce","Unit"};
    public static int [] unitIcons = {
            R.drawable.cup,
            R.drawable.tablespoon,
            R.drawable.teaspoon,
            R.drawable.g,
            R.drawable.kilogram,
            R.drawable.oz,
            R.drawable.unit
    };
    public static int [] recipeIcons = {
            R.drawable.nutella_pie,
            R.drawable.brownie,
            R.drawable.yellow_cake,
            R.drawable.cheese_cake
    };

}