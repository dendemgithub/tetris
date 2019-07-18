package com.tetris.model;

import java.util.Random;

public enum Piece {
    O(PointSets.O),
    I(PointSets.I),
    ;

    private Point[][] pointSets;
    private int currentPosition;
    private final int positionsCount;

    private static final Random rnd = new Random();

    Piece(Point[][] pointSets) {
        this.pointSets = pointSets;
        positionsCount = pointSets.length;
        currentPosition = 0;
    }

    public void rotate() {
        currentPosition++;
        if (currentPosition == positionsCount) currentPosition = 0;
    }

    public Point[] getPoints() {
        return pointSets[currentPosition];
    }

    public static Piece getRandomPiece() {
        return values()[rnd.nextInt(values().length)];
    }
}

class Point {
    private int x;
    private int y;

    Point(int x, int y) {
        setCoordinates(x, y);
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class PointSets {
    static final Point[][] O = {{new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)}};

    static final Point[][] I = {
            {new Point(0, 1), new Point(0, 0), new Point(0, -1), new Point(0, -2)},
            {new Point(-1, 0), new Point(0, 0), new Point(1, 0), new Point(2, 0)}
    };
}