package com.example.snake.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snake.apples.abstracts.AbstractApple;
import com.example.snake.snake.Snake;

public class GreenApple extends AbstractApple {
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
