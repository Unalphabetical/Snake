package com.example.snake.states;

public class GameState {

    private volatile boolean playing = false;
    private volatile boolean paused = true;

    private volatile boolean dead = false;
    private volatile boolean showPausedText = false;

    public boolean isPaused() {
        return paused;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isShowingPausedText() {
        return showPausedText;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setShowPausedText(boolean showPausedText) {
        this.showPausedText = showPausedText;
    }

}
