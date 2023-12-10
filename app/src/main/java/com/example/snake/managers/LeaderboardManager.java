package com.example.snake.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snake.snake.Snake;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaderboardManager {

    private List<Snake> snakeList;
    private SharedPreferences prefs;
    private Gson gson;

    private boolean sorted = false;

    private static final String LEADERBOARD_KEY = "snakeLeaderboard";

    public LeaderboardManager(Context context) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.prefs = context.getSharedPreferences("com.example.snakio.leaderboard", Context.MODE_PRIVATE);
        this.snakeList = Arrays.asList(new Snake[5]);
    }

    public List<Snake> getSnakeList() {
        return snakeList;
    }

    public void deleteData() {
        this.prefs.edit().clear().apply();
        this.snakeList = Arrays.asList(new Snake[5]);
    }

    public void saveSnakeList() {
        String snakeLeaderboardData = gson.toJson(snakeList);
        this.prefs.edit().putString(LEADERBOARD_KEY, snakeLeaderboardData).apply();
    }

    public void loadSnakeList() {
        String appleData = this.prefs.getString(LEADERBOARD_KEY, "null");
        Type listType = new TypeToken<ArrayList<Snake>>(){}.getType();
        this.snakeList = this.gson.fromJson(appleData, listType);
    }

    public boolean sort() {
        if (snakeList == null) return false;
        if (snakeList.size() == 0) return false;
        if (sorted) return true;

        snakeList.sort((o1, o2) -> {
            if (o1 == null || o2 == null) return 0;
            else return o2.getScore() - o1.getScore();
        });
        sorted = true;
        return true;
    }

    public void addSnake(Snake snake) {
        if (getSnakeList() == null) this.snakeList = Arrays.asList(new Snake[5]);;
        if (getSnakeList().isEmpty()) return;

        int snakeToReplace = Integer.MAX_VALUE - 1;
        for (int i = 0; i < snakeList.size(); i++) {
            if (snakeList.get(i) == null || snakeList.get(i).getScore() < snake.getScore()) {
                snakeToReplace = i;
                sorted = false;
                break;
            }
        }
        if (snakeToReplace < snakeList.size()) snakeList.set(snakeToReplace, snake);
    }

    public void prettyPrintSnakeList() {
        if (snakeList == null) return;
        System.out.println("Leaderboard:");
        for (int i = 0; i < snakeList.size(); i++) {
            if (snakeList.get(i) == null) continue;
            System.out.println(i + 1 + ". " + snakeList.get(i).getScore());
        }
    }

}
