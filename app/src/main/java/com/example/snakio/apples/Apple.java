package com.example.snakio.apples;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Apple {

    // The location of the apple on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an apple
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the apple
    private Bitmap mBitmapApple;

    // Add a reference to SnakeAudio instance
    private SnakeAudio snakeAudio;

    int apple;

    private long expireDuration;
    private long expireTime;

    /// Set up the apple in the constructor
    public Apple(Context context, Point sr, int s, int apple, SnakeAudio snakeAudio){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;

        // Make a note of the size of an apple
        mSize = s;

        // Hide the apple off-screen until the game starts
        location.x = -10;

        //// Make a note of the apple to refresh the bitmap
        this.apple = apple;

        this.snakeAudio = snakeAudio;

        refreshBitmap(context);

    }

    public Apple setValidity(int seconds) {
        this.expireDuration = seconds * 1000L;
        return updateValidity();
    }

    public Apple updateValidity() {
        this.expireTime = System.currentTimeMillis() + this.expireDuration;
        return this;
    }

    // This is called every time an apple is eaten
    public Apple spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = (int) (random.nextInt((int) (mSpawnRange.x * 0.5)) + (mSpawnRange.x * 0.25));
        location.y = random.nextInt(mSpawnRange.y - 2) + 1;
        snakeAudio.playSpawnAppleSound();
        return this;
    }

    // Let SnakeGame know where the apple is
    // SnakeGame can share this with the snake
    public Point getLocation(){
        return location;
    }

    // Draw the apple
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapApple,
                location.x * mSize, location.y * mSize, paint);
    }

    //// This is required to refresh the bitmap
    //// after the Apple is saved and loaded back into memory
    public Apple refreshBitmap(Context context) {
        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), apple);

        // Resize the bitmap
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, mSize, mSize, false);

        return this;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expireTime;
    }

    public boolean onEaten(Object... objects) {
        return false;
    }

}
