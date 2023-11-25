package com.example.snakio.managers;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.apples.Apple;
import com.example.snakio.R;
import com.example.snakio.apples.types.RedApple;

import java.util.ArrayList;
import java.util.List;

public class AppleManager {

    Context context;
    List<Apple> appleList = new ArrayList<>();

    public AppleManager(Context context, Point sr, int s, int appleCount) {
        this.context = context;
        for (int i = 0; i < appleCount; i++) {
            Apple apple = new RedApple(context, sr, s, R.drawable.redapple).setValidity(10).refreshBitmap(context);
            this.appleList.add(apple);
        }
    }

    public List<Apple> getAppleList() {
        return appleList;
    }

    public void setAppleList(List<Apple> appleList) {
        this.appleList = appleList;
    }

    public AppleManager spawnApple() {
        for (Apple apple : appleList) {
            apple.refreshBitmap(this.context).spawn();
        }
        return this;
    }

}
