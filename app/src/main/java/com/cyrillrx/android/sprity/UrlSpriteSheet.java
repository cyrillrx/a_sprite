package com.cyrillrx.android.sprity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import com.cyrillrx.android.toolbox.logger.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * s
 *
 * @author Cyril Leroux
 *         Created on 09/02/14.
 */
public class UrlSpriteSheet extends SpriteSheet {

    private static final String TAG = UrlSpriteSheet.class.getSimpleName();

    public UrlSpriteSheet(String url, int columnCount, int rowCount) {
        super(columnCount, rowCount);
        asyncImageLoad(url);
    }

    private void asyncImageLoad(String url) {

        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) { }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Logger.error(TAG, "onLoadingFailed " + imageUri + ". " + failReason, failReason.getCause());
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                setBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Logger.debug(TAG, "onLoadingCancelled " + imageUri);

            }
        });
    }

    public void draw(Canvas canvas, int pos) {

        if (mBitmap == null) {
            canvas.drawColor(Color.GRAY);
            return;
        }

        super.draw(canvas, pos);
    }
}
