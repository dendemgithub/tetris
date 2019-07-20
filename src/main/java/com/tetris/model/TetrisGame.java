package com.tetris.model;

public class TetrisGame {
    private Board board;
    private Piece nextPiece;
    private boolean started = false;
    private boolean paused;
    private int scores;
    private boolean gameOver;

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

        if(!board.moveDown()) {
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
        nextPiece = new Piece(PieceType.getRandomPiece());
    }

    private void updateScores(int linesBurnt) {
        scores += (Math.pow(2, linesBurnt) - 1) * 100;
    }

    public void userInput(UserCommands command) {
        if(paused && !command.equals(UserCommands.PAUSE)) return;

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
        }
    }

    @Override
    public String toString() {
        String board = this.board.toString();
        board += String.format("\nScores: %d\n", scores);
        if(gameOver) board += "GAME OVER!\n";
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < Board.WIDTH; i++)
            buffer.append('-');
        return board + buffer.toString();
    }
}
