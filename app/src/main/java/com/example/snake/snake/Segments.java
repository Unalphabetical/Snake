package com.example.snake.snake;

import android.graphics.Point;

public class Segments {

    public enum Type {
        HEAD, BODY
    }

    private Point point;
    private Type type;

    public Segments(Point point, Type type) {
        this.point = point;
        this.type = type;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public Type getType() {
        return type;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }

}
