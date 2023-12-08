package com.example.snake.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class SnakeCanvas {

    SurfaceHolder surfaceHolder;
    Paint paint;
    Canvas canvas;

    public SnakeCanvas(SurfaceHolder surfaceHolder, Paint paint) {
        this.surfaceHolder = surfaceHolder;
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public int getX() {
        //// Calculate the middle of the screen
        //// with the font size included
        return (canvas.getWidth() / 2);
    }

    public int getY() {
        //// Calculate the middle of the screen
        //// with the font size included
        return (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
    }

    public boolean lock() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Fill the screen with a color
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            return true;
        }

        return false;
    }

    public void unlock() {
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void drawGameOver(int score) {
        initializePaint();
        int x = getX();
        int y = getY();

        canvas.drawText("Game Over!", x, y - 100, paint);
        canvas.drawText("Score: " + score, x, y + 100, paint);
    }

    public void drawTapToPlay() {
        initializePaint();
        int x = getX();
        int y = getY();

        canvas.drawText("Tap To Play!", x, y, paint);
    }

    public void initializePaint() {
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setTextSize(200);
        paint.setTextAlign(Paint.Align.CENTER);
    }

}
