package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;
import com.example.snakio.snake.Snake;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlueApple extends Apple {

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
