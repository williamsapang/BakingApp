package com.example.samsophias.bakingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MyDialUtility {

    private static AlertDialog dialog;
    private static AlertDialog dialogWithButtons;

    public static void closeDialog() {
        dialog.dismiss();
    }

    public static void showDialogWithButtons(final Context context, int resId, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dial_buttons, null);
        builder.setView(customView);
        CardView cardView = customView.findViewById(R.id.cardview_dialog);
        ImageView dialogImage = customView.findViewById(R.id.image_dialog);
        ImageView dialogClose = customView.findViewById(R.id.close_dialog);
        TextView dialogText = customView.findViewById(R.id.textview_dialog);
        Button dialogButtonRetry = customView.findViewById(R.id.button_retry);
        Button dialogButtonExit = customView.findViewById(R.id.button_exit);
        Picasso.with(context).load(resId).into(dialogImage);
        dialogText.setText(message);
        cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundIngredients));
        dialogWithButtons = builder.create();
        dialogButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWithButtons.dismiss();
                ((Activity)context).finish();
            }
        });
        dialogButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWithButtons.dismiss();
                Intent intent = ((Activity) context).getIntent();
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWithButtons.dismiss();
            }
        });
        dialogWithButtons.show();
    }
}