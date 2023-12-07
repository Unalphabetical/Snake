package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;
import com.example.snakio.snake.SnakeHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrangeApple extends Apple {

    public OrangeApple(Context context, Point sr, int s, int apple) {
        super(context, sr, s, apple);
    }

    //// The snake can wrap around the screen for 10 seconds
    //// Currently it is not cumulative
    //// So if you eat another one, the timer won't reset
    @Override
    public boolean onEaten(Object... objects) {
        for (Object o : objects) {
            if (o instanceof SnakeHandler) {
                SnakeHandler snakeHandler = (SnakeHandler) o;
                snakeHandler.setWrapAround(true);

                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> snakeHandler.setWrapAround(false), 10, TimeUnit.SECONDS);
                scheduler.shutdown();

                return true;
            }
        }
        return false;
    }

}
