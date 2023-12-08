package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;

public class RedApple extends Apple {

    public RedApple(Context context, Point spawnRange, int size, int apple) {
        super(context, spawnRange, size, apple);
    }

    @Override
    public boolean onEaten(Object... objects) {
        return false;
    }

}
