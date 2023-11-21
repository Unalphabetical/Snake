package com.example.snakio.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snakio.snake.Snake;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveManager {

    SharedPreferences prefs;
    Gson gson;

    Snake snake;

    public SaveManager(Context context) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.prefs = context.getSharedPreferences("com.example.snakio", Context.MODE_PRIVATE);
    }

    public Snake getSnake() {
        return snake;
    }
    public SaveManager setSnake(Snake snake) {
        this.snake = snake;
        return this;
    }

    public void saveSnakeData() {
        String snakeData = this.gson.toJson(this.snake);
        this.prefs.edit().putString("snakeData", snakeData).apply();
    }

    public void loadSnakeData() {
        String snakeData = this.prefs.getString("snakeData", "null");
        this.snake = this.gson.fromJson(snakeData, Snake.class);
    }

    public boolean hasData() {
        String snakeData = this.prefs.getString("snakeData", "null");

        return !snakeData.equals("null");
    }

    public void save() {
        this.saveSnakeData();
    }

    public void load() {
        if (hasData()) {
            this.loadSnakeData();
        }
    }

    public void deleteData() {
        this.prefs.edit().clear().apply();
    }

}
