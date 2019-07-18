package com.tetris.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void toStringTest() {
        Board board = new Board();
        board.createNewPice();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();

        board.rotatePiece();
        board.createNewPice();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        board.moveDown();
        System.out.println(board.toString());
    }
}