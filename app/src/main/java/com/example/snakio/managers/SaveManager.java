package com.example.snakio.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snakio.apples.Apple;
import com.example.snakio.snake.Snake;
import com.example.snakio.states.GameState;
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

    List<Apple> appleList;

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

    public List<Apple> getAppleList() {
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

    public SaveManager setAppleList(List<Apple> appleList) {
        this.appleList = appleList;
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

    public void saveHeadingData() {
        this.prefs.edit().putString("headingData", this.heading).apply();
    }

    public void saveAppleData() {
        String appleData = this.gson.toJson(this.appleList);
        this.prefs.edit().putString("appleData", appleData).apply();
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
        String snakeData = this.prefs.getString("snakeData", "null");
        this.snake = this.gson.fromJson(snakeData, Snake.class);
    }

    public void loadGameState() {
        String gameStateData = this.prefs.getString("gameStateData", "null");
        this.gameState = this.gson.fromJson(gameStateData, GameState.class);
    }

    public void loadHeadingData() {
        this.heading = this.prefs.getString("headingData", "null");
    }

    public void loadAppleData() {
        String appleData = this.prefs.getString("appleData", "null");
        Type listType = new TypeToken<ArrayList<Apple>>(){}.getType();
        this.appleList = this.gson.fromJson(appleData, listType);
    }

    public boolean isMusicEnabled() {
        return prefs.getBoolean(MUSIC_ENABLED_KEY, true);
    }

    public boolean isSoundEnabled(){
        return prefs.getBoolean(SOUND_ENABLED_KEY, true);
    }

    public boolean hasData() {
        String snakeData = this.prefs.getString("snakeData", "null");
        String gameStateData = this.prefs.getString("gameStateData", "null");
        String headingData = this.prefs.getString("headingData", "null");
        String appleData = this.prefs.getString("appleData", "null");

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
