package com.example.snakio.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snakio.snake.Snake;
import com.example.snakio.states.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveManager {

    SharedPreferences prefs;
    Gson gson;

    Snake snake;
    GameState gameState;

    public SaveManager(Context context) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.prefs = context.getSharedPreferences("com.example.snakio", Context.MODE_PRIVATE);
    }

    public Snake getSnake() {
        return snake;
    }

    public GameState getGameState() {
        return gameState;
    }

    public SaveManager setSnake(Snake snake) {
        this.snake = snake;
        return this;
    }

    public SaveManager setGameState(GameState gameState) {
        this.gameState = gameState;
        return this;
    }

    public void saveSnakeData() {
        String snakeData = this.gson.toJson(this.snake);
        this.prefs.edit().putString("snakeData", snakeData).apply();
    }

    public void saveGameState() {
        String gameStateData = this.gson.toJson(this.gameState);
        this.prefs.edit().putString("gameStateData", gameStateData).apply();
    }

    public void loadSnakeData() {
        String snakeData = this.prefs.getString("snakeData", "null");
        this.snake = this.gson.fromJson(snakeData, Snake.class);
    }

    public void loadGameState() {
        String gameStateData = this.prefs.getString("gameStateData", "null");
        this.gameState = this.gson.fromJson(gameStateData, GameState.class);
    }

    public boolean hasData() {
        String snakeData = this.prefs.getString("snakeData", "null");
        String gameStateData = this.prefs.getString("gameStateData", "null");

        return !snakeData.equals("null") && !gameStateData.equals("null");
    }

    public void save() {
        this.saveSnakeData();
        this.saveGameState();
    }

    public void load() {
        if (hasData()) {
            this.loadSnakeData();
            this.loadGameState();
        }
    }

    public void deleteData() {
        this.prefs.edit().clear().apply();
    }

}
