package com.cyrillrx.android.sprity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

import com.cyrillrx.android.toolbox.logger.LogCat;
import com.cyrillrx.android.toolbox.logger.Logger;
import com.cyrillrx.android.toolbox.logger.Severity;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MEMORY_CACHE_SIZE = 2 * 1024 * 1024;
    private static final int COLUMN_COUNT = 25;
    private static final int ROW_COUNT = 1;

    private ThumbnailView mThumbnail;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApp(this);

        mThumbnail = (ThumbnailView) findViewById(R.id.surface_view);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        final Resources res = getResources();
        final List<SpriteSheet> sheets = new ArrayList<>();

        sheets.add(new SpriteSheet(res, R.drawable.light_1, COLUMN_COUNT, ROW_COUNT));
        sheets.add(new SpriteSheet(res, R.drawable.light_2, COLUMN_COUNT, ROW_COUNT));
        sheets.add(new SpriteSheet(res, R.drawable.light_3, COLUMN_COUNT, ROW_COUNT));
        sheets.add(new SpriteSheet(res, R.drawable.light_4, COLUMN_COUNT, ROW_COUNT));
        sheets.add(new SpriteSheet(res, R.drawable.light_5, COLUMN_COUNT, ROW_COUNT));
        sheets.add(new SpriteSheet(res, R.drawable.light_6, COLUMN_COUNT, ROW_COUNT));
        sheets.add(new SpriteSheet(res, R.drawable.light_7, COLUMN_COUNT, ROW_COUNT));
        sheets.add(new SpriteSheet(res, R.drawable.light_8, COLUMN_COUNT, ROW_COUNT));

        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-1.png", COLUMN_COUNT, ROW_COUNT));
        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-2.png", COLUMN_COUNT, ROW_COUNT));
        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-3.png", COLUMN_COUNT, ROW_COUNT));
        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-4.png", COLUMN_COUNT, ROW_COUNT));
        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-5.png", COLUMN_COUNT, ROW_COUNT));
        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-6.png", COLUMN_COUNT, ROW_COUNT));
        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-7.png", COLUMN_COUNT, ROW_COUNT));
        sheets.add(new UrlSpriteSheet("http://thumb.molotov.tv/2e/62/08/000afce69fd053dc71e5cba5aeb3f79d9908622e/000afce69fd053dc71e5cba5aeb3f79d9908622e-thumbnail-8.png", COLUMN_COUNT, ROW_COUNT));

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

    private static void initApp(Context context) {

        Logger.initialize(context);
        Logger.addChild(new LogCat(Severity.VERBOSE));
        initImageEngine(context);
    }

    /**
     * Initializes the image caching engine.
     *
     * @see <a href="https://github.com/nostra13/Android-Universal-Image-Loader">Universal Image Loader</a>.
     */
    private static void initImageEngine(Context context) {

        // Create global configuration and initialize ImageLoader with this config
        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(4)
                .memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE))
                .memoryCacheSize(MEMORY_CACHE_SIZE)
                .defaultDisplayImageOptions(getDefaultOptions())
                .build();
        L.writeLogs(true);

        ImageLoader.getInstance().init(config);

    }

    /**
     * @return The default options which will be used for every images.
     */
    private static DisplayImageOptions getDefaultOptions() {

        final ColorDrawable loadingDrawable = new ColorDrawable(Color.GRAY);
        final ColorDrawable errorDrawable = new ColorDrawable(Color.RED);
        final ColorDrawable emptyDrawable = new ColorDrawable(Color.DKGRAY);

        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(loadingDrawable)
                .showImageOnFail(errorDrawable)
                .showImageForEmptyUri(emptyDrawable)
                .build();
    }

}
