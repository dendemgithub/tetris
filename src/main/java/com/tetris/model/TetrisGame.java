package com.tetris.model;

import java.util.Random;

public class TetrisGame {
    private Board board = new Board();
    private Piece nextPiece;
    private boolean started = false;
    private boolean paused;
    private int scores;
    private boolean gameOver;
    private int totalLinesBurnt;


    public void start() {
        board = new Board();
        scores = 0;
        started = true;
        gameOver = false;
        board.createNewPiece(new Piece(PieceType.getRandomPiece()));
        createNewPiece();
    }

    public Board getBoard() {
        return board;
    }

    public void tick() {
        if (gameOver) return;
        if (!started) return;
        if (paused) return;

        if (!board.moveDown()) {
            int linesBurnt = board.finishTurn();
            if (linesBurnt < 0) {
                gameOver = true;
            } else {
                updateScores(linesBurnt);
                board.createNewPiece(nextPiece);
                createNewPiece();
            }
        }
    }

    private void createNewPiece() {
        PieceType type = PieceType.getRandomPiece();
        nextPiece = new Piece(type, new Random().nextInt(1000), new Random().nextInt(type.getPositionsCount()));
    }

    public Piece getNextPiece() {
        return nextPiece;
    }

    private void updateScores(int linesBurnt) {
        totalLinesBurnt += linesBurnt;
        scores += (Math.pow(2, linesBurnt) - 1) * 100;
    }

    public void userInput(UserCommands command) {
        if (command == null) return;
        if (paused && !command.equals(UserCommands.PAUSE)) return;

        switch (command) {
            case DROP:
                break;
            case LEFT:
                board.moveLeft();
                break;
            case RIGHT:
                board.moveRight();
                break;
            case ROTATE:
                board.rotate();
                break;
            case PAUSE:
                paused = !paused;
                break;
            case START:
                start();
                break;
            // cheats
            case CHEAT_ROTATE_NEXT:
                rotateNext();
                break;
        }
    }

    public int getScores() {
        return scores;
    }

    public int getLinesBurnt() {
        return totalLinesBurnt;
    }

    @Override
    public String toString() {
        String board = this.board.toString();
        board += String.format("\nScores: %d\n", scores);
        if (gameOver) board += "GAME OVER!\n";
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < Board.COLUMNS; i++)
            buffer.append('-');
        return board + buffer.toString();
    }

    //cheats
    public void rotateNext() {
        nextPiece.rotate();
    }
}
