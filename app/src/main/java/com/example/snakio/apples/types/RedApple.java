package com.example.snakio.apples.types;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.SnakeAudio;
import com.example.snakio.apples.Apple;

public class RedApple extends Apple {

    public RedApple(Context context, Point sr, int s, int apple, SnakeAudio snakeAudio) {
        super(context, sr, s, apple, snakeAudio);
    }

    @Override
    public boolean onEaten(Object... objects) {
        return false;
    }

}
