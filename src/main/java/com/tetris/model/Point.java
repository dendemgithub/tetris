package com.tetris.model;

public class Point {
    private int x;
    private int y;
    private int color;

    Point(int color) {
        this.color = color;
    }

    public void setCoordinates(int[] coordinates) {
        this.x = coordinates[0];
        this.y = coordinates[1];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }
}
