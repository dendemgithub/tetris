package com.tetris.model;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGTH = 20;
    private static final int effectiveHeigth = HEIGTH + 4;

    private Cell[][] cells = new Cell[WIDTH][effectiveHeigth];
    private Piece currentPiece;
    private int pieceX;
    private int pieceY;

    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int i = HEIGTH - 1; i >= 0; i--) {
            for (int j = 0; j < WIDTH; j++) {
                board.append(cells[j][i] == null ? "." : "X");
            }
            board.append("\n");
        }
        for (int i = 0; i < WIDTH; i++) board.append("--");
        return board.toString();
    }

    public void createNewPice() {
        currentPiece = Piece.getRandomPiece();
        pieceX = WIDTH/2;
        pieceY = HEIGTH - 1;
        placePiece();
    }

    public void erasePiece() {
        for (Point point: currentPiece.getPoints()) {
            cells[point.getX() + pieceX][point.getY() + pieceY] = null;
        }
    }

    public void placePiece() {
        for (Point point: currentPiece.getPoints()) {
            cells[point.getX() + pieceX][point.getY() + pieceY] = new Cell();
        }
    }

    private void move(int x, int y) {
        erasePiece();
        pieceX += x;
        pieceY += y;
        placePiece();
    }

    public void moveDown() {
        move(0, -1);
    }

    public void moveLeft() {
        move(-1, 0);
    }

    public void moveRight() {
        move(1, 0);
    }

    public void rotatePiece() {
        erasePiece();
        currentPiece.rotate();
        placePiece();
    }
}

class Cell {

}