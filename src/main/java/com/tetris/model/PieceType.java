package com.tetris.model;

import java.util.Random;

public enum PieceType {
    O(PointSets.O),
    I(PointSets.I),
    ;

    private int[][][] pointSets;
    private final int positionsCount;

    private static final Random rnd = new Random();

    PieceType(int[][][] pointSets) {
        this.pointSets = pointSets;
        positionsCount = pointSets.length;
    }

    public int[][] getPoints(int position) {
        return pointSets[position];
    }

    public static PieceType getRandomPiece() {
        return values()[rnd.nextInt(values().length)];
    }

    int getPositionsCount() {
        return positionsCount;
    }

    private static class PointSets {
        static final int[][][] O = {{{0, 0}, {0,1}, {1,0}, {1,1}}};
        static final int[][][] I = {
                {{0, 1}, {0,0}, {0, -1}, {0, -2}},
                {{-1, 0}, {0,0}, {1, 0}, {2, 0}},
        };
    }
}

