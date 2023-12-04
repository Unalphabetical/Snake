package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;
import com.example.snakio.snake.Snake;

import java.util.Timer;
import java.util.TimerTask;

public class BlueApple extends Apple {

    public BlueApple(Context context, Point sr, int s, int apple) {
        super(context, sr, s, apple);
    }

    @Override
    public boolean onEaten(Object... objects) {
        for (Object o : objects) {
            if (o instanceof Snake) {
                Snake snake = (Snake) o;
                snake.setInverted(true);

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        snake.setInverted(false);
                    }
                };

                Timer timer = new Timer();
                timer.schedule(timerTask, 10000L);
            }
        }
        return true;
    }

}
