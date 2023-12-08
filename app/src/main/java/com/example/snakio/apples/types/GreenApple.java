package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;
import com.example.snakio.snake.Snake;

public class GreenApple extends Apple {
    public GreenApple(Context context, Point spawnRange, int size, int apple) {
        super(context, spawnRange, size, apple);
    }

    //// The snake restarts
    @Override
    public boolean onEaten(Object... object) {
        for (Object o : object) {
            if (o instanceof Snake) {
                Snake snake = (Snake) o;
                snake.restart();
                return true;
            }
        }
        return false;
    }

}
