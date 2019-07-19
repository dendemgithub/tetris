package com.tetris.model;

import org.junit.Test;

public class BoardTest {

    @Test
    public void boardTest() {
        Board board = new Board();
        board.createNewPiece(Piece.I);
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();

        board.rotate();
        board.createNewPiece(Piece.I);
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        System.out.println(board.toString());
    }
}