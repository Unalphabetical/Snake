package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;
import com.example.snakio.snake.SnakeHandler;

import java.util.Timer;
import java.util.TimerTask;

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

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        snakeHandler.setWrapAround(false);
                    }
                };

                Timer timer = new Timer();
                timer.schedule(timerTask, 10000L);
            }
        }
        return true;
    }

}
