package com.example.snake.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snake.apples.abstracts.AbstractApple;

public class RedApple extends AbstractApple {

    public RedApple(Context context, Point spawnRange, int size, int apple) {
        super(context, spawnRange, size, apple);
    }

    @Override
    public boolean onEaten(Object... objects) {
        return false;
    }

}
