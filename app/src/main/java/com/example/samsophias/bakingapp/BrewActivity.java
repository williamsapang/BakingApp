package com.example.samsophias.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class BrewActivity extends AppCompatActivity  implements View.OnClickListener
        , MyStepNumAdapter.OnStepClick{

    public static final String STEP_LIST_STATE = "step_list_state";
    public static final String STEP_NUMBER_STATE = "step_number_state";
    public static final String STEP_LIST_JSON_STATE = "step_list_json_state";
    private boolean isTablet;
    private int mVideoNumber = 0;
    Button mButtonNext;
    Button mButtonPrevious;
    RecyclerView mRecyclerSteps;
    FrameLayout frameLayout;


    ArrayList<Step> mStepArrayList = new ArrayList<>();
    String mJsonResult;
    boolean isFromWidget;
    MyStepNumAdapter mMyStepNumAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(findViewById(R.id.cooking_tablet) != null){
            isTablet = true;

        } else{
            isTablet = false;
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MyConsUtility.STEP_INTENT_EXTRA)) {
                mStepArrayList = getIntent().getParcelableArrayListExtra(MyConsUtility.STEP_INTENT_EXTRA);
            }
            if (intent.hasExtra(MyConsUtility.JSON_RESULT_EXTRA)) {
                mJsonResult = getIntent().getStringExtra(MyConsUtility.JSON_RESULT_EXTRA);
            }
            if(intent.getStringExtra(MyConsUtility.WIDGET_EXTRA) != null){
                isFromWidget = true;
            }
            else{
                isFromWidget = false;
            }
        }

        if(savedInstanceState == null){
            playVideo(mStepArrayList.get(mVideoNumber));
        }

        ButterKnife.bind(this);
        frameLayout = findViewById(R.id.framelayout_container);
        mButtonNext = findViewById(R.id.button_next);
        mButtonPrevious = findViewById(R.id.button_previous);
        mRecyclerSteps = findViewById(R.id.rv_recipe_steps);
        handleUiForDevice();
    }

    @Override
    public void onClick(View v) {

        if(mVideoNumber == mStepArrayList.size()-1){
            Toast.makeText(this, R.string.brewing_is_over, Toast.LENGTH_SHORT).show();
        } else{
            if(v.getId() == mButtonPrevious.getId()){
                mVideoNumber--;
                if(mVideoNumber < 0){
                    Toast.makeText(this, R.string.see_next_step, Toast.LENGTH_SHORT).show();
                }
                else
                    playVideoReplace(mStepArrayList.get(mVideoNumber));
            }
            else if(v.getId() == mButtonNext.getId()){
                mVideoNumber++;
                playVideoReplace(mStepArrayList.get(mVideoNumber));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_STATE, mStepArrayList);
        outState.putString(STEP_LIST_JSON_STATE, mJsonResult);
        outState.putInt(STEP_NUMBER_STATE, mVideoNumber);
    }

    @Override
    public void onStepClick(int position) {
        mVideoNumber = position;
        playVideoReplace(mStepArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void playVideo(Step step){
        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(MyConsUtility.STEP_SINGLE, step);
        exoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.framelayout_container, exoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }


    public void playVideoReplace(Step step){
        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(MyConsUtility.STEP_SINGLE, step);
        exoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout_container, exoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStepArrayList = savedInstanceState.getParcelableArrayList(STEP_LIST_STATE);
            mJsonResult = savedInstanceState.getString(STEP_LIST_JSON_STATE);
            mVideoNumber = savedInstanceState.getInt(STEP_NUMBER_STATE);
        }
    }

    public void handleUiForDevice(){
        if(!isTablet){
            mButtonNext.setOnClickListener(this);
            mButtonPrevious.setOnClickListener(this);
        } else{
            mMyStepNumAdapter = new MyStepNumAdapter(this,mStepArrayList, this, mVideoNumber);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerSteps.setLayoutManager(mLinearLayoutManager);
            mRecyclerSteps.setAdapter(mMyStepNumAdapter);
        }
    }

}