package com.tetris.model;

import java.util.*;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGTH = 20;
    private static final int effectiveHeigth = HEIGTH + 4;

    private Cell[][] cells = new Cell[effectiveHeigth][WIDTH];
    private Piece currentPiece;
    private int pieceX;
    private int pieceY;

    private boolean gameOver;

    Board() {
        for(int i = 0; i < effectiveHeigth; i++ )
            for(int j = 0; j < WIDTH; j++)
                cells[i][j] = new Cell();

        gameOver = false;
    }


    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int i = HEIGTH - 1; i >= 0; i--) {
            for (int j = 0; j < WIDTH; j++) {
                board.append(cells[i][j].getPoint() == null ? "." : "X");
            }
            board.append("\n");
        }
        for (int i = 0; i < WIDTH; i++) board.append("--");
        return board.toString();
    }

    void createNewPiece(Piece piece) {
        currentPiece = piece;
        pieceX = WIDTH/2;
        pieceY = HEIGTH - 1;
        placePiece();
    }

    private boolean erasePiece() {
        for (Point point: currentPiece.getPoints()) {
            cells[point.getY() + pieceY][point.getX() + pieceX].clear();
        }
        return true;
    }

    private void placePiece() {
        for (Point point: currentPiece.getPoints()) {
            cells[point.getY() + pieceY][point.getX() + pieceX].setPoint(point);
        }
    }

    private boolean canBePlaced(int newX, int newY) {
        int x, y;
        for (Point point: currentPiece.getPoints()) {
            x = point.getX() + newX;
            y = point.getY() + newY;
            if (x < 0 || x > WIDTH - 1) return false;
            if (y < 0 || y > effectiveHeigth - 1) return false;
            if (cells[y][x].isSolid()) return false;
        }
        return true;
    }

    private boolean move(int x, int y) {
        int newX = pieceX + x;
        int newY = pieceY + y;
        if (canBePlaced(newX, newY)) {
            erasePiece();
            pieceX = newX;
            pieceY = newY;
            placePiece();
            return true;
        } else {
            return false;
        }
    }

    boolean moveDown() {
        return move(0, -1);
    }

    public void moveLeft() {
        move(-1, 0);
    }

    public void moveRight() {
        move(1, 0);
    }

    public void rotate() {
        erasePiece();
        currentPiece.rotate();
        if (!canBePlaced(pieceX, pieceY)) currentPiece.rotateBack();
        placePiece();
    }

    /**
     * Burn lines. Check gameover.
     * Return -1 if gameover. Otherwise return number of lines burnt.
     * */

    public int finishTurn() {
        for (Point point: currentPiece.getPoints()) {
            cells[point.getY() + pieceY][point.getX() + pieceX].setSolid();
        }
        int linesBurnt = burnLines();
        return checkGameOver() ? -1 : linesBurnt;
    }

    /**
     * Burn lines if possible.
     * Return number of lines burnt.
     * */
    private int burnLines() {
        List<Integer> linesToBurn = new ArrayList<Integer>(4);
        Cell[] line;
        for(int i = 0; i < effectiveHeigth; i++) {
            line = cells[i];
            if(Arrays.stream(line).filter(Cell::isSolid).count() == WIDTH) linesToBurn.add(i);
        }
        Map<Integer, Integer> updatedIndexMap = new HashMap<>();
        for(int oldIndex = 0; oldIndex < effectiveHeigth; oldIndex++) {
            int decrease = 0;
            for(int lineToBurnIndex: linesToBurn) {
                if (oldIndex > lineToBurnIndex) decrease++;
                updatedIndexMap.put(oldIndex, oldIndex-decrease);
            }
        }
        for(int oldIndex = 0; oldIndex < effectiveHeigth; oldIndex++) {
            Integer newIndex = updatedIndexMap.get(oldIndex);
            if (newIndex != null) cells[newIndex] = cells[oldIndex];
        }
        for(int i = effectiveHeigth - linesToBurn.size(); i < effectiveHeigth; i++)
            for(int j = 0; j < WIDTH; j++)
                cells[i][j] = new Cell();
        return linesToBurn.size();
    }

    private boolean checkGameOver() {
        for (int i = HEIGTH; i < effectiveHeigth; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (cells[i][j].isSolid()) {
                    gameOver = true;
                    return true;
                }
            }
        }
        return gameOver;
    }
}

class Cell {
    private Point point;

    private boolean solid = false;

    Point getPoint() {
        return point;
    }

    void setPoint(Point point) {
        this.point = point;
    }

    void clear() {
        point = null;
        solid = false;
    }

    public boolean isSolid() {
        return getPoint() != null && solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    public void setSolid() {
        setSolid(true);
    }
}