package com.example.snake.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snake.snake.SnakeEngine;
import com.example.snake.apples.abstracts.AbstractApple;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PurpleApple extends AbstractApple {

    public PurpleApple(Context context, Point spawnRange, int size, int apple) {
        super(context, spawnRange, size, apple);
    }

    // The game doubles in speed for 10 seconds and then returns to normal
    @Override
    public boolean onEaten(Object... objects) {
        for (Object o : objects) {
            if (o instanceof SnakeEngine) {
                SnakeEngine snakeEngine = (SnakeEngine) o;
                snakeEngine.setSpeed(20);

                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> snakeEngine.setSpeed(10), 10, TimeUnit.SECONDS);
                scheduler.shutdown();

                return true;
            }
        }
        return false;
    }
}
