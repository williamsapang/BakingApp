package com.example.samsophias.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class MyWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    static void updateAppWidget(Context context, String jsonRecipeIngredients, int imgResId, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget_provider);

        Intent intent = new Intent(context, MainDetailsActivity.class);
        intent.putExtra(MyConsUtility.WIDGET_EXTRA,"CAME_FROM_WIDGET");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        if(jsonRecipeIngredients.equals("")){
            jsonRecipeIngredients = "No ingredients yet!";
        }

        views.setTextViewText(R.id.widget_ingredients, jsonRecipeIngredients);
        views.setImageViewResource(R.id.MyWidgetIcon, imgResId);

        views.setOnClickPendingIntent(R.id.widget_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyWidgetService.startActionOpenRecipe(context);
    }

    public static void updateWidgetRecipe(Context context, String jsonRecipe , int imgResId, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, jsonRecipe, imgResId, appWidgetManager, appWidgetId);
        }
    }

}

