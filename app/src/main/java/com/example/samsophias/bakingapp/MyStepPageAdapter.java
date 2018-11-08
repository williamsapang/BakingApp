package com.example.samsophias.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyStepPageAdapter extends FragmentPagerAdapter {

    ArrayList<Step> mStepList;
    Bundle stepsBundle = new Bundle();

    public MyStepPageAdapter(FragmentManager fm, ArrayList<Step> stepList) {
        super(fm);
        this.mStepList = stepList;
    }

    @Override
    public int getCount() {
        return mStepList.size();
    }

    @Override
    public Fragment getItem(int position) {
        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
        stepsBundle.putParcelableArrayList("steps", mStepList);
        stepsBundle.putInt("page",position+1);
        stepsBundle.putBoolean("isLastPage",position == getCount() - 1);
        exoPlayerFragment.setArguments(stepsBundle);

        return exoPlayerFragment;
    }
}