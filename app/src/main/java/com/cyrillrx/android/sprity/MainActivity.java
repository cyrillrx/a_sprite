package com.cyrillrx.android.sprity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ThumbnailView mThumbnail;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mThumbnail = (ThumbnailView) findViewById(R.id.surface_view);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        final Resources res = getResources();

        List<SpriteSheet> sheets = new ArrayList<>();
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_1, 25, 1));
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_2, 25, 1));
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_3, 25, 1));
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_4, 25, 1));
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_5, 25, 1));
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_6, 25, 1));
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_7, 25, 1));
        sheets.add(new SpriteSheet(res, R.drawable.thumbnail_8, 25, 1));

        mThumbnail.setSpriteSheets(sheets);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                final Rect thumbRect = seekBar.getThumb().getBounds();
                Log.d(TAG, thumbRect.toShortString());

                float newPosX = thumbRect.centerX() - seekBar.getX() - mThumbnail.getWidth() / 2f;
                float newPosY = seekBar.getTop() - thumbRect.height() - mThumbnail.getHeight();

                mThumbnail.onChangePosition(progress, seekBar.getMax(), newPosX, newPosY);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mThumbnail.startTracking();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mThumbnail.stopTracking();
            }
        });

    }

}
