package ua.ddemianyk.games.tetris.model;

public class Piece {
    private int currentPosition;

    private PieceType type;
    private Point[] points;
    private int color;

    Piece(PieceType type) {
        this(type, 0);
    }

    Piece(PieceType type, int color) {
        this(type, color, 0);
    }

    Piece(PieceType type, int color, int initialPostion) {
        this.type = type;
        currentPosition = initialPostion;
        this.color = color;
        createPoints();
    }

    private void createPoints() {
        points = new Point[type.getPoints(currentPosition).length];
        for(int i = 0; i < points.length; i++)
            points[i] = new Point(color);
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

    public Point[] getPoints() {
        return points;
    }


}