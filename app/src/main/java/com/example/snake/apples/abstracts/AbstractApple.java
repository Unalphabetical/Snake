package com.example.snake.apples.abstracts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class AbstractApple {

    // The location of the apple on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an apple
    private Point spawnRange;
    private int size;

    // An image to represent the apple
    private Bitmap mBitmapApple;

    int apple;

    private long expireDuration;
    private long expireTime;

    /// Set up the apple in the constructor
    public AbstractApple(Context context, Point spawnRange, int size, int apple){

        // Make a note of the passed in spawn range
        this.spawnRange = spawnRange;

        // Make a note of the size of an apple
        this.size = size;

        // Hide the apple off-screen until the game starts
        this.location.x = -10;

        //// Make a note of the apple to refresh the bitmap
        this.apple = apple;

        refreshBitmap(context);

    }

    public AbstractApple setValidity(int seconds) {
        this.expireDuration = seconds * 1000L;
        return updateValidity();
    }

    public AbstractApple updateValidity() {
        this.expireTime = System.currentTimeMillis() + this.expireDuration;
        return this;
    }

    // This is called every time an apple is eaten
    public AbstractApple spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = (int) (random.nextInt((int) (spawnRange.x * 0.5)) + (spawnRange.x * 0.25));
        location.y = random.nextInt(spawnRange.y - 2) + 1;
        return this;
    }

    // Let SnakeEngine know where the apple is
    // SnakeEngine can share this with the snake
    public Point getLocation(){
        return location;
    }

    // Draw the apple
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapApple,
                location.x * size, location.y * size, paint);
    }

    //// This is required to refresh the bitmap
    //// after the AbstractApple is saved and loaded back into memory
    public AbstractApple refreshBitmap(Context context) {
        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), apple);

        // Resize the bitmap
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, size, size, false);

        return this;
    }

    //// Checks whether the apple is expired or not
    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expireTime;
    }

    //// This event is called when a snake eats an apple
    //// allowing us to make the Apples have powerups
    public boolean onEaten(Object... objects) {
        return false;
    }

}
