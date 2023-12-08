package com.example.snake.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snake.apples.abstracts.AbstractApple;
import com.example.snake.snake.Snake;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlueApple extends AbstractApple {

    public BlueApple(Context context, Point spawnRange, int size, int apple) {
        super(context, spawnRange, size, apple);
    }

    //// The controls for the snake are inverted for 10 seconds
    @Override
    public boolean onEaten(Object... objects) {
        for (Object o : objects) {
            if (o instanceof Snake) {
                Snake snake = (Snake) o;
                snake.setInverted(true);

                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> snake.setInverted(false), 10, TimeUnit.SECONDS);

                scheduler.shutdown();
                return true;
            }
        }
        return false;
    }

}
