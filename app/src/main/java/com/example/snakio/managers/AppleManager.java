package com.example.snakio.managers;

import android.content.Context;
import android.graphics.Point;

import com.example.snakio.R;
import com.example.snakio.apples.Apple;
import com.example.snakio.apples.types.BlueApple;
import com.example.snakio.apples.types.GreenApple;
import com.example.snakio.apples.types.OrangeApple;
import com.example.snakio.apples.types.RedApple;
import com.example.snakio.apples.types.PurpleApple;

import java.util.ArrayList;
import java.util.List;

public class AppleManager {

    Context context;
    Point point;
    int size;
    List<Apple> appleList = new ArrayList<>();

    public AppleManager(Context context, Point point, int size, int appleCount) {
        this.point = point;
        this.size = size;

        this.context = context;
        for (int i = 0; i < appleCount; i++) {
            Apple apple = getRandomApple();
            this.appleList.add(apple);
        }
    }

    public List<Apple> getAppleList() {
        return this.appleList;
    }

    public void setAppleList(List<Apple> appleList) {
        this.appleList = appleList;
    }

    public Apple getRandomApple() {
        List<Apple> appleChoice = loadApples();
        List<Integer> appleChance = loadChances();

        Apple apple = appleChoice.get(0);
        for (Integer chance : appleChance) {
            int random = (int) (Math.random() * 1000);
            if (random <= chance) {
                apple = appleChoice.get(appleChance.indexOf(chance));
            }
        }
        return apple;
    }

    public AppleManager spawnApple() {
        for (Apple apple : appleList) {
            apple.refreshBitmap(context).spawn();
        }
        return this;
    }

    public List<Apple> loadApples() {
        List<Apple> appleChoice = new ArrayList<>();
        appleChoice.add(0, new RedApple(context, point, size, R.drawable.redapple).setValidity(10).refreshBitmap(context));
        appleChoice.add(1, new BlueApple(context, point, size, R.drawable.blueapple).setValidity(10).refreshBitmap(context));
        appleChoice.add(2, new GreenApple(context, point, size, R.drawable.greenapple).setValidity(10).refreshBitmap(context));
        appleChoice.add(3, new OrangeApple(context, point, size, R.drawable.orangeapple).setValidity(10).refreshBitmap(context));
        appleChoice.add(4, new PurpleApple(context, point, size, R.drawable.purpleapple).setValidity(10).refreshBitmap(context));
        return appleChoice;
    }

    public List<Integer> loadChances() {
        List<Integer> appleChance = new ArrayList<>();
        appleChance.add(0, 1000);
        appleChance.add(1, 150);
        appleChance.add(2, 100);
        appleChance.add(3, 75);
        appleChance.add(4, 50);
        return appleChance;
    }

    public void replaceApple(Apple apple) {
        int index = appleList.indexOf(apple);
        Apple randomApple = getRandomApple().spawn();
        appleList.set(index, randomApple);

    }

}
