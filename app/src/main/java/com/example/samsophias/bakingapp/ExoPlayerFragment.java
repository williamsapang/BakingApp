package com.example.samsophias.bakingapp;


import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExoPlayerFragment extends Fragment {


    public static final String STEP_URI =  "step_uri";
    public static final String STEP_VIDEO_POSITION =  "step_video_position";
    public static final String STEP_PLAY_WHEN_READY =  "step_play_when_ready";
    public static final String STEP_PLAY_WINDOW_INDEX =  "step_play_window_index";
    public static final String STEP_SINGLE =  "step_single";

    TextView mStepTitle;

    SimpleExoPlayerView exoPlayerView;


    TextView mStepDescription;


    SimpleExoPlayer simpleExoPlayer;

    Step mStep;
    Uri mUri;
    String mThumbnail;
    Bitmap mThumbnailImage;
    boolean mShouldPlayWhenReady = true;
    long mPlayerPosition;
    int mWindowIndex;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ExoPlayerFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putString(STEP_URI, mStep.getVideoURL());
        outState.putParcelable(STEP_SINGLE, mStep);
        outState.putLong(STEP_VIDEO_POSITION, mPlayerPosition);
        outState.putBoolean(STEP_PLAY_WHEN_READY, mShouldPlayWhenReady);
    }

    public void initializeVideoPlayer(Uri videoUri){

        mStepDescription.setText(mStep.getDescription());
        mStepTitle.setText(mStep.getShortDescription());

        if(simpleExoPlayer == null){

            simpleExoPlayer =  ExoPlayerFactory.newSimpleInstance(getActivity(),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            exoPlayerView.setPlayer(simpleExoPlayer);
            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            if (mPlayerPosition != C.TIME_UNSET) {
                simpleExoPlayer.seekTo(mPlayerPosition);
            }
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(mShouldPlayWhenReady);
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            updateStartPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializeVideoPlayer(mUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            initializeVideoPlayer(mUri);
        }
        if(simpleExoPlayer != null){
            simpleExoPlayer.setPlayWhenReady(mShouldPlayWhenReady);
            simpleExoPlayer.seekTo(mPlayerPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer != null){
            updateStartPosition();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(simpleExoPlayer != null){
            updateStartPosition();
            if (Util.SDK_INT > 23) {
                releasePlayer();
            }
        }
    }

    private void updateStartPosition() {
        if (simpleExoPlayer != null) {
            mShouldPlayWhenReady = simpleExoPlayer.getPlayWhenReady();
            mWindowIndex = simpleExoPlayer.getCurrentWindowIndex();
            mPlayerPosition = simpleExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exo_player, container, false);
        ButterKnife.bind(this, root);
        mStepTitle = root.findViewById(R.id.step_title);
        exoPlayerView = root.findViewById(R.id.exo_player);
        mStepDescription = root.findViewById(R.id.step_description);

        if(savedInstanceState != null){
            mStep = savedInstanceState.getParcelable(STEP_SINGLE);
            mShouldPlayWhenReady = savedInstanceState.getBoolean(STEP_PLAY_WHEN_READY);
            mPlayerPosition = savedInstanceState.getLong(STEP_VIDEO_POSITION);
            mWindowIndex = savedInstanceState.getInt(STEP_PLAY_WINDOW_INDEX);
            mUri = Uri.parse(savedInstanceState.getString(STEP_URI));
        } else{
            if(getArguments() != null){

                exoPlayerView.setVisibility(View.VISIBLE);
                mStep = getArguments().getParcelable(MyConsUtility.STEP_SINGLE);
                if(mStep.getVideoURL().equals("")){
                    if(mStep.getThumbnailURL().equals("")){
                        exoPlayerView.setUseArtwork(true);

                        exoPlayerView.setUseController(false);
                    } else{
                        exoPlayerView.setVisibility(View.VISIBLE);
                        mThumbnail = mStep.getThumbnailURL();
                        mThumbnailImage = ThumbnailUtils.createVideoThumbnail(mThumbnail, MediaStore.Video.Thumbnails.MICRO_KIND);
                        exoPlayerView.setUseArtwork(true);
                        exoPlayerView.setDefaultArtwork(mThumbnailImage);
                    }
                } else{
                    mUri = Uri.parse(mStep.getVideoURL());
                }
            }
        }
        return root;
    }

}
