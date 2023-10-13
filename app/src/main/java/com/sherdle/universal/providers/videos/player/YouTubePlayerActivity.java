/**
 * Copyright 2013 The Finest Artist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sherdle.universal.providers.videos.player;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.sherdle.universal.R;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 *  This activity is used when the user clicks the play button, it is the in-app-native video player
 */

public class YouTubePlayerActivity extends AppCompatActivity {
    public static final String EXTRA_VIDEO_ID = "video_id";

    private YouTubePlayer youTubePlayer;
    @Override
    public void onBackPressed() {
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        FrameLayout fullscreenViewContainer = findViewById(R.id.full_screen_view_container);

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1) // enable full screen button
                .build();

        // we need to initialize manually in order to pass IFramePlayerOptions to the player
        youTubePlayerView.setEnableAutomaticInitialization(false);

        youTubePlayerView.addFullscreenListener(new FullscreenListener() {

            @Override
            public void onEnterFullscreen(@NotNull View fullscreenView, @NotNull Function0<Unit> exitFullscreen) {

                // the video will continue playing in fullscreenView
                youTubePlayerView.setVisibility(View.GONE);
                fullscreenViewContainer.setVisibility(View.VISIBLE);
                fullscreenViewContainer.addView(fullscreenView);

                // optionally request landscape orientation
                // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            @Override
            public void onExitFullscreen() {

                // the video will continue playing in the player
                youTubePlayerView.setVisibility(View.VISIBLE);
                fullscreenViewContainer.setVisibility(View.GONE);
                fullscreenViewContainer.removeAllViews();
            }
        });

        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer initializedYouTubePlayer) {
                YouTubePlayerActivity.this.youTubePlayer = initializedYouTubePlayer;
                String mVideoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
                youTubePlayer.loadVideo(mVideoId, 0f);

                youTubePlayer.toggleFullscreen();
            }
        }, iFramePlayerOptions);

        getLifecycle().addObserver(youTubePlayerView);

    }
}




