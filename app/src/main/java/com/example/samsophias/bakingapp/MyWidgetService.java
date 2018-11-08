package com.example.samsophias.bakingapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;

import java.util.List;



public class MyWidgetService extends IntentService {
    public static final String ACTION_OPEN_RECIPE =
            "com.example.samsophias.bakingapp.my_widget_service";


    public MyWidgetService(String name) {
        super(name);
    }
    public MyWidgetService() {
        super("MyWidgetService");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_RECIPE.equals(action)) {
                handleActionOpenRecipe();
            }
        }
    }

    public static void startActionOpenRecipeO(Context context){
        Intent intent = new Intent(context, MyWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        ContextCompat.startForegroundService(context,intent);
    }

    private void handleActionOpenRecipe() {
        SharedPreferences sharedpreferences =
                getSharedPreferences(MyConsUtility.BAKINGAPP_SHARED_PREF,MODE_PRIVATE);
        String jsonRecipe = sharedpreferences.getString(MyConsUtility.JSON_RESULT_EXTRA, "");
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);
        int id= recipe.getId();
        int imgResId = MyConsUtility.recipeIcons[id-1];
        List<Ingredient> ingredientList = recipe.getIngredients();
        for(Ingredient ingredient : ingredientList){
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();
            String line = quantity + " " + measure + " " + ingredientName;
            stringBuilder.append( line + "\n");
        }
        String ingredientsString = stringBuilder.toString();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MyWidgetProvider.class));
        MyWidgetProvider.updateWidgetRecipe(this, ingredientsString, imgResId, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Bakingapp service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, MyWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }

}
