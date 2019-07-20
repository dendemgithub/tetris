package com.tetris.model;

public class Cell {
    private Point point;

    private boolean solid = false;

    public Point getPoint() {
        return point;
    }

    void setPoint(Point point) {
        this.point = point;
    }

    void clear() {
        point = null;
        solid = false;
    }

    boolean isSolid() {
        return getPoint() != null && solid;
    }

    void makeSolid() {
        this.solid = true;
    }
}