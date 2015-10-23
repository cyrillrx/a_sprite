package com.cyrillrx.android.sprity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * s
 *
 * @author Cyril Leroux
 *         Created on 09/02/14.
 */
public class SpriteSheet {

    private final Bitmap mBitmap;
    private final Rect[][] mFrameGrid;
    private final Rect[] mFrameList;

    public SpriteSheet(Bitmap spriteSheet, int columnCount, int rowCount) {
        mBitmap = spriteSheet;
        mFrameGrid = getRectGrid(spriteSheet, columnCount, rowCount);
        mFrameList = getRectList(spriteSheet, columnCount, rowCount);
    }

    /**
     * Constructor that will load a bitmap from the resources.
     *
     * @param resources  Context resources used to load sprite sheet bitmap.
     * @param resourceId The id of the sprite sheet resource.
     */
    public SpriteSheet(Resources resources, int resourceId, int columnCount, int rowCount) {
        this(BitmapFactory.decodeResource(resources, resourceId, getBitmapOptions()), columnCount, rowCount);
    }

    private static BitmapFactory.Options getBitmapOptions() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
        return options;
    }

    private static Rect[][] getRectGrid(final Bitmap spriteSheet, int columnCount, int rowCount) {

        Rect[][] grid = new Rect[rowCount][columnCount];
        final int frameWidth = spriteSheet.getWidth() / columnCount;
        final int frameHeight = spriteSheet.getHeight() / rowCount;

        int currentX = 0;
        int currentY = 0;

        // For each line
        for (int y = 0; y < rowCount; y++) {
            // For each element in the line
            for (int x = 0; x < columnCount; x++) {
                grid[y][x] = new Rect(currentX, currentY, currentX + frameWidth, currentY + frameHeight);
                currentX += frameWidth;
            }
            // End of the line : reset x, update y
            currentX = 0;
            currentY += frameHeight;
        }
        return grid;
    }

    private static Rect[] getRectList(final Bitmap spriteSheet, int columnCount, int rowCount) {

        Rect[] list = new Rect[rowCount * columnCount];
        final int frameWidth = spriteSheet.getWidth() / columnCount;
        final int frameHeight = spriteSheet.getHeight() / rowCount;

        int currentX = 0;
        int currentY = 0;
        int it = 0;

        // For each line
        for (int y = 0; y < rowCount; y++) {
            // For each element in the line
            for (int x = 0; x < columnCount; x++) {
                list[it] = new Rect(currentX, currentY, currentX + frameWidth, currentY + frameHeight);
                currentX += frameWidth;
                it++;
            }
            // End of the line : reset x, update y
            currentX = 0;
            currentY += frameHeight;
        }
        return list;
    }

    public Bitmap getBitmap() { return mBitmap; }

    public Rect getRect(int y, int x) { return mFrameGrid[y][x]; }

    public Rect getRect(int pos) { return mFrameList[pos]; }

    public int getSpriteCount() { return mFrameList.length; }

    public void draw(Canvas canvas, int pos) {
        final Rect src = mFrameList[pos];
        final Rect dest = canvas.getClipBounds();
        canvas.drawBitmap(mBitmap, src, dest, null);
    }
}
