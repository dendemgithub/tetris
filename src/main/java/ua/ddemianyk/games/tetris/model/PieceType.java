package ua.ddemianyk.games.tetris.model;

import java.util.Random;

public enum PieceType {
    O(PointSets.O),
    I(PointSets.I),
    L(PointSets.L),
    J(PointSets.J),
    Z(PointSets.Z),
    S(PointSets.S),
    T(PointSets.T),
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
        static final int[][][] O = {{{-1, -1}, {-1,0}, {0,-1}, {0,0}}};
        static final int[][][] I = {
                {{-2, 0}, {-1, 0}, {0, 0}, {1, 0}},
                {{0, 1}, {0, 0}, {0, -1}, {0, -2}},
                {{-2, -1}, {-1, -1}, {0, -1}, {1, -1}},
                {{-1, 1}, {-1, 0}, {-1, -1}, {-1, -2}},
        };
        static final int[][][] J = {
                {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}},
                {{-2, 0}, {-1, 0}, {0, 0}, {0, -1}},
                {{-1, 1}, {-1, 0}, {-1, -1}, {-2, -1}},
                {{0, 0}, {-1, 0}, {-2, 0}, {-2, 1}},
        };
        static final int[][][] L = {
                {{-1, 1}, {-1, 0}, {-1, -1}, {0, -1}},
                {{0, 0}, {-1, 0}, {-2, 0}, {-2, -1}},
                {{-1, -1}, {-1, 0}, {-1, 1}, {-2, 1}},
                {{-2, 0}, {-1, 0}, {0, 0}, {0, 1}},
        };
        static final int[][][] Z = {
                {{-1, -1}, {-1, 0}, {0, 0}, {0, 1}},
                {{-2, 0}, {-1, 0}, {-1, -1}, {0, -1}},
                {{-1, 1}, {-1, 0}, {-2, 0}, {-2, -1}},
                {{0, 0}, {-1, 0}, {-1, 1}, {-2, 1}},
        };
        static final int[][][] S = {
                {{-1, 1}, {-1, 0}, {0, 0}, {0, -1}},
                {{0, 0}, {-1, 0}, {-1, -1}, {-2, -1}},
                {{-1, -1}, {-1, 0}, {-2, 0}, {-2, 1}},
                {{-2, 0}, {-1, 0}, {-1, 1}, {0, 1}},
        };
        static final int[][][] T = {
                {{-1, 1}, {-1, 0}, {-1, -1}, {0, 0}},
                {{0, 0}, {-1, 0}, {-2, 0}, {-1, -1}},
                {{-1, -1}, {-1, 0}, {-1, 1}, {-2, 0}},
                {{-2, 0}, {-1, 0}, {0, 0}, {-1, 1}},
        };
    }
}

