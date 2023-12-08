package com.example.snake.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snake.apples.abstracts.AbstractApple;
import com.example.snake.snake.Snake;
import com.example.snake.states.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {

    SharedPreferences prefs;
    Gson gson;

    Snake snake;
    GameState gameState;
    String heading;

    List<AbstractApple> appleList;

    private static final String SNAKE_DATA_KEY = "snakeData";
    private static final String GAME_STATE_DATA_KEY = "gameStateData";
    private static final String HEADING_DATA_KEY = "headingData";
    private static final String APPLE_DATA_KEY = "appleData";
    private static final String MUSIC_ENABLED_KEY = "musicEnabled";
    private static final String SOUND_ENABLED_KEY = "soundEnabled";

    public SaveManager(Context context) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.prefs = context.getSharedPreferences("com.example.snakio.data", Context.MODE_PRIVATE);
    }

    public Snake getSnake() {
        return snake;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getHeading() {
        return heading;
    }

    public List<AbstractApple> getAppleList() {
        return appleList;
    }

    public SaveManager setSnake(Snake snake) {
        this.snake = snake;
        return this;
    }

    public SaveManager setGameState(GameState gameState) {
        this.gameState = gameState;
        return this;
    }

    public SaveManager setHeading(String heading) {
        this.heading = heading;
        return this;
    }

    public SaveManager setAppleList(List<AbstractApple> appleList) {
        this.appleList = appleList;
        return this;
    }

    public void saveSnakeData() {
        String snakeData = this.gson.toJson(this.snake);
        this.prefs.edit().putString(SNAKE_DATA_KEY, snakeData).apply();
    }

    public void saveGameState() {
        String gameStateData = this.gson.toJson(this.gameState);
        this.prefs.edit().putString(GAME_STATE_DATA_KEY, gameStateData).apply();
    }

    public void saveHeadingData() {
        this.prefs.edit().putString(HEADING_DATA_KEY, this.heading).apply();
    }

    public void saveAppleData() {
        String appleData = this.gson.toJson(this.appleList);
        this.prefs.edit().putString(APPLE_DATA_KEY, appleData).apply();
    }

    public SaveManager setMusicEnabled(boolean musicEnabled) {
        prefs.edit().putBoolean(MUSIC_ENABLED_KEY, musicEnabled).apply();
        return this;
    }

    public SaveManager setSoundEnabled(boolean soundEnabled){
        prefs.edit().putBoolean(SOUND_ENABLED_KEY, soundEnabled).apply();
        return this;
    }

    public void loadSnakeData() {
        String snakeData = this.prefs.getString(SNAKE_DATA_KEY, "null");
        this.snake = this.gson.fromJson(snakeData, Snake.class);
    }

    public void loadGameState() {
        String gameStateData = this.prefs.getString(GAME_STATE_DATA_KEY, "null");
        this.gameState = this.gson.fromJson(gameStateData, GameState.class);
    }

    public void loadHeadingData() {
        this.heading = this.prefs.getString(HEADING_DATA_KEY, "null");
    }

    public void loadAppleData() {
        String appleData = this.prefs.getString(APPLE_DATA_KEY, "null");
        Type listType = new TypeToken<ArrayList<AbstractApple>>(){}.getType();
        this.appleList = this.gson.fromJson(appleData, listType);
    }

    public boolean isMusicEnabled() {
        return prefs.getBoolean(MUSIC_ENABLED_KEY, true);
    }

    public boolean isSoundEnabled(){
        return prefs.getBoolean(SOUND_ENABLED_KEY, true);
    }

    public boolean hasData() {
        String snakeData = this.prefs.getString(SNAKE_DATA_KEY, "null");
        String gameStateData = this.prefs.getString(GAME_STATE_DATA_KEY, "null");
        String headingData = this.prefs.getString(HEADING_DATA_KEY, "null");
        String appleData = this.prefs.getString(APPLE_DATA_KEY, "null");

        return !snakeData.equals("null")
                && !gameStateData.equals("null")
                && !headingData.equals("null")
                && !appleData.equals("null");
    }

    public void save() {
        this.saveSnakeData();
        this.saveGameState();
        this.saveHeadingData();
        this.saveAppleData();
        this.setMusicEnabled(this.isMusicEnabled());
        this.setSoundEnabled(this.isSoundEnabled());
    }

    public void load() {
        if (hasData()) {
            this.loadSnakeData();
            this.loadGameState();
            this.loadHeadingData();
            this.loadAppleData();
        }
    }

    public void deleteData() {
        this.prefs.edit().clear().apply();
    }

}
