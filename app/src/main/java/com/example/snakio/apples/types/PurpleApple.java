package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.SnakeGame;
import com.example.snakio.apples.Apple;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PurpleApple extends Apple {

    public PurpleApple(Context context, Point sr, int s, int apple) {
        super(context, sr, s, apple);
    }

    // The game doubles in speed for 10 seconds and then returns to normal
    @Override
    public boolean onEaten(Object... objects) {
        for (Object o : objects) {
            if (o instanceof SnakeGame) {
                SnakeGame snakeGame = (SnakeGame) o;
                snakeGame.setSpeed(20);

                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> snakeGame.setSpeed(10), 10, TimeUnit.SECONDS);
                scheduler.shutdown();

                return true;
            }
        }
        return false;
    }
}
