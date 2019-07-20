package com.tetris.model;

class Piece {
    private int currentPosition;

    private PieceType type;
    private Point[] points;

    Piece(PieceType type) {
        this.type = type;
        currentPosition = 0;
        createPoints();
    }

    private void createPoints() {
        points = new Point[type.getPoints(currentPosition).length];
        for(int i = 0; i < points.length; i++)
            points[i] = new Point();
        setPointPositions();
    }

    private void setPointPositions() {
        int[][] positions = type.getPoints(currentPosition);
        for(int i = 0; i < type.getPoints(currentPosition).length; i++) {
            points[i].setCoordinates(positions[i]);
        }
    }

    void rotate() {
        currentPosition++;
        if (currentPosition == type.getPositionsCount()) currentPosition = 0;
        setPointPositions();
    }

    void rotateBack() {
        currentPosition--;
        if (currentPosition == -1) currentPosition = type.getPositionsCount() - 1;
        setPointPositions();
    }

    Point[] getPoints() {
        return points;
    }


}

class Point {
    private int x;
    private int y;

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
}