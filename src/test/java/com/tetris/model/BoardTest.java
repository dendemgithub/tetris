package com.tetris.model;

import org.junit.Test;

public class BoardTest {

    @Test
    public void boardTest() {
        Board board = new Board();
        //board.createNewPiece(new Piece(PieceType.I));
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();

        board.rotate();
        //board.createNewPiece(new Piece(PieceType.I));
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        System.out.println(board.toString());
    }
}