package ua.ddemianyk.games.tetris.model;

import java.util.*;

public class Board {
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    private static final int effectiveRows = ROWS + 4;

    private Cell[][] cells = new Cell[effectiveRows][COLUMNS];
    private Piece currentPiece;
    private int pieceX;
    private int pieceY;

    private boolean gameOver;

    Board() {
        for(int i = 0; i < effectiveRows; i++ )
            for(int j = 0; j < COLUMNS; j++)
                cells[i][j] = new Cell();

        gameOver = false;
    }


    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLUMNS; j++) {
                board.append(getCells()[i][j].getPoint() == null ? "." : "X");
            }
            board.append("\n");
        }
        for (int i = 0; i < COLUMNS; i++) board.append("--");
        return board.toString();
    }

    void createNewPiece(Piece piece) {
        currentPiece = piece;
        pieceX = COLUMNS /2;
        pieceY = ROWS - 1;
        placePiece();
    }

    private void erasePiece() {
        for (Point point: currentPiece.getPoints()) {
            cells[point.getY() + pieceY][point.getX() + pieceX].clear();
        }
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
            if (x < 0 || x > COLUMNS - 1) return false;
            if (y < 0 || y > effectiveRows - 1) return false;
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

    void moveLeft() {
        move(-1, 0);
    }

    void moveRight() {
        move(1, 0);
    }

    void rotate() {
        erasePiece();
        currentPiece.rotate();
        if (!canBePlaced(pieceX, pieceY)) currentPiece.rotateBack();
        placePiece();
    }

    /**
     * Burn lines. Check gameover.
     * Return -1 if gameover. Otherwise return number of lines burnt.
     * */

    int finishTurn() {
        for (Point point: currentPiece.getPoints()) {
            cells[point.getY() + pieceY][point.getX() + pieceX].makeSolid();
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
        for(int i = 0; i < effectiveRows; i++) {
            line = cells[i];
            if(Arrays.stream(line).filter(Cell::isSolid).count() == COLUMNS) linesToBurn.add(i);
        }
        Map<Integer, Integer> updatedIndexMap = new HashMap<>();
        for(int oldIndex = 0; oldIndex < effectiveRows; oldIndex++) {
            int decrease = 0;
            for(int lineToBurnIndex: linesToBurn) {
                if (oldIndex > lineToBurnIndex) decrease++;
                updatedIndexMap.put(oldIndex, oldIndex-decrease);
            }
        }
        for(int oldIndex = 0; oldIndex < effectiveRows; oldIndex++) {
            Integer newIndex = updatedIndexMap.get(oldIndex);
            if (newIndex != null) cells[newIndex] = cells[oldIndex];
        }
        for(int i = effectiveRows - linesToBurn.size(); i < effectiveRows; i++) {
            cells[i] = new Cell[COLUMNS];
            for (int j = 0; j < COLUMNS; j++)
                cells[i][j] = new Cell();
        }
        return linesToBurn.size();
    }

    private boolean checkGameOver() {
        for (int i = ROWS; i < effectiveRows; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (cells[i][j].isSolid()) {
                    gameOver = true;
                    return true;
                }
            }
        }
        return gameOver;
    }

    public Cell[][] getCells() {
        return Arrays.copyOfRange(cells, 0, ROWS);
    }
}