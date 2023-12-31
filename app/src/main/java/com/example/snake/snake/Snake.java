package com.example.snake.snake;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    int score;
    public List<Segments> segments;
    public boolean inverted = false;

    public Snake() {
        this.segments = new ArrayList<>();
        this.score = 0;
    }

    public List<Segments> getSegments() {
        return segments;
    }

    public int getScore() {
        return score;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void deleteSegments() {
        this.segments = new ArrayList<>();
    }

    public void grow(Point point, Segments.Type bodyType) {
        Segments segment = new Segments(point, bodyType);
        this.segments.add(segment);
    }

    public void reset() {
        deleteSegments();
        this.score = 0;
    }

    public void restart() {
        int x = this.segments.get(0).getX();
        int y = this.segments.get(0).getY();
        deleteSegments();
        grow(new Point(x, y), Segments.Type.HEAD);
        this.score = 0;
    }

}
