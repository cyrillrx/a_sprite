package com.cyrillrx.android.sprity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * @author Cyril Leroux
 *         Created on 22/10/15
 */
public class ThumbnailView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = ThumbnailView.class.getSimpleName();

    private DrawingThread mDrawingThread;
    private List<SpriteSheet> mSpriteSheets;
    private SpriteSheet mSpriteSheet;
    private int mSpriteId;

    public ThumbnailView(Context context) {
        super(context);
    }

    public ThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbnailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ThumbnailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSpriteSheets(List<SpriteSheet> spriteSheets) {
        mSpriteSheets = spriteSheets;

        mDrawingThread = new DrawingThread(getHolder());
        mDrawingThread.start();
    }

    public void startTracking() {
        setVisibility(VISIBLE);

    }

    public void stopTracking() {
        setVisibility(GONE);
    }

    public void onChangePosition(float progress, float max, float newX, float newY) {
        final int sheetCount = mSpriteSheets.size();

        if (progress == max) {
            mSpriteSheet = mSpriteSheets.get(sheetCount - 1);
            mSpriteId = mSpriteSheet.getSpriteCount() - 1;
            return;
        }

        int sheetId = (int) (progress / max * sheetCount);
        mSpriteSheet = mSpriteSheets.get(sheetId);
        float positionsPerSheet = max / sheetCount;
        float offset = positionsPerSheet * sheetId;
        mSpriteId = (int) ((progress - offset) / positionsPerSheet * mSpriteSheet.getSpriteCount());

        setX(newX);
        setY(newY);
        mDrawingThread.redraw();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mDrawingThread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public class DrawingThread extends Thread {

        private final SurfaceHolder mSurfaceHolder;

        /** Number of frame we wish to draw per second. */
        private final static int FRAME_RATE = 20;
        /** The time a frame is suppose to stay on screen in milliseconds. */
        private int FRAME_DURATION = 1000 / FRAME_RATE;
        /** Indicate whether the thread is suppose to draw or not. */
        private boolean mRunning = true;
        private boolean mRedraw = true;

        public DrawingThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {

            mRunning = true;

            while (mRunning) {
                long start = System.currentTimeMillis();

                if (mRedraw) {
                    draw();
                    mRedraw = false;
                }

                final long waitingTimeMillis = FRAME_DURATION - (System.currentTimeMillis() - start);
                if (waitingTimeMillis > 0) {
                    try {
                        sleep(waitingTimeMillis);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "sleep error in the drawing thread", e);
                        mRunning = false;
                    }
                } else {
                    // We are running late !
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Running late ! " + waitingTimeMillis);
                    }
                }
            }
        }

        /**
         * Used to signal the thread whether it should be running or not.
         *
         * @param running true to run, false to shut down
         */
        public void setRunning(boolean running) { mRunning = running; }

        public void redraw() { mRedraw = true; }

        /** Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                updateSurfaceSize(width, height);
            }
        }

        /**
         * Update the surface size atomically.<br />
         * Synchronized is performed by the caller ({@link #setSurfaceSize(int, int)}).
         */
        protected void updateSurfaceSize(int surfaceWidth, int surfaceHeight) {

        }

        /** Draw the new frame. */
        private void draw() {
            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);
                if (canvas != null) {
                    doDraw(canvas);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        /**
         * Draws current state of the canvas.<br />
         * Canvas null check is performed by the caller ({@link #draw()}).
         */
        protected void doDraw(Canvas canvas) {
            mSpriteSheet.draw(canvas, mSpriteId);
        }
    }
}
