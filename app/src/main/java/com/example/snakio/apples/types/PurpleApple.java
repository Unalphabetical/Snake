package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;
import com.example.snakio.SnakeGame;

import java.util.Timer;
import java.util.TimerTask;

 public class PurpleApple extends Apple {
    
    public PurpleApple(Context context, Point sr, int s, int apple) {
        super(context, sr, s, apple);
    }

    // Going to add event here to give a little speed boost to the snake when eating this apple
    @Override
    public boolean onEaten(Object... objects) {
        for (Object o : objects) {
            if (o instanceof SnakeGame) {
                SnakeGame snakeGame = (SnakeGame) o;
                snakeGame.setSpeed(20);

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        snakeGame.setSpeed(10);
                    }
                };

                Timer timer = new Timer();
                timer.schedule(timerTask, 10000L);
            }
        }
        return true;
    }
}

