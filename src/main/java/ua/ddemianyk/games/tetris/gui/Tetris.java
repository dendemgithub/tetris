package ua.ddemianyk.games.tetris.gui;

import ua.ddemianyk.games.tetris.keyboard.KeyboardListner;
import ua.ddemianyk.games.tetris.model.*;
import ua.ddemianyk.games.tetris.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Tetris extends Canvas implements Runnable {
    private static final String name = "Tetris";
    private static final int HEIGHT = 600;
    private static final int WIDTH = 600;

    private int FPS = 60;
    private double defaultTPS = 3; // see below
    private int dropTPS = 60; // defaultTPS for "drop mode"
    private boolean dropping = false;
    private double nanosPerTick = 1000 * 1000 * 1000 / defaultTPS;
    private int CPS = 50; // how frequently user input is processed

    private int boardHeight;
    private int boardWidth;
    private int cellSize;
    private int boardTop;
    private int boardBottom;
    private int boardLeft;
    private int boardRight;
    private ArrayList<Color> pieceColors = new ArrayList<>();

    private int previewTop;
    private int previewBottom;
    private int previewLeft;
    private int previewRight;
    private int previewWidth;
    private int previewHeight;

    private int statsTop;
    private int statsBottom;
    private int statsLeft;
    private int statsRight;
    private int statsWidth;
    private int statsHeight;

    private boolean running;
    private TetrisGame game;

    private BufferStrategy strategy;
    private Graphics2D graphics;
    private KeyboardListner keyboard = new KeyboardListner();


    private Tetris() {
        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.setSize(dimension);
        setBounds(0, 0, WIDTH, HEIGHT);
        this.addKeyListener(keyboard);

        JFrame frame = new JFrame(name);
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(dimension);
        panel.add(this);

        panel.setLayout(null);
        frame.setSize(dimension);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        initBoardParams();
        initPreviewParams();
        initStatParams();
        prepareColorList();

        createBufferStrategy(2);
        strategy = this.getBufferStrategy();
        graphics = (Graphics2D) strategy.getDrawGraphics();
        game = new TetrisGame();
    }

    public static void main(String... args) {
        new Tetris().start();
    }

    private void gameLoop() {
        game.tick();
        //System.out.println(game.toString());
    }

    private void render() {
        graphics = (Graphics2D) strategy.getDrawGraphics();
        graphics.clearRect(0, 0, WIDTH, HEIGHT);

        drawBoard();
        drawPreview();
        drawStats();

        strategy.show();
        graphics.dispose();
    }


    @Override
    public void run() {
        long now;
        long lastTime = System.nanoTime();
        double nanosPerFrame = 1000 * 1000 * 1000 / FPS;
        double nanosPerCommand = 1000 * 1000 * 1000 / CPS;
        double frameDelta = 0;
        double tickDelta = 0;
        double userInputDelta = 0;

        long timer = 0;
        int frames = 0;
        int ticks = 0;

        while (running) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            now = System.nanoTime();
            frameDelta += (now - lastTime) / nanosPerFrame;
            tickDelta += (now - lastTime) / nanosPerTick;
            userInputDelta += (now - lastTime) / nanosPerCommand;

            timer += now - lastTime;
            lastTime = now;

            if (frameDelta >= 1) {
                frameDelta--;
                render();
                frames++;
            }

            if (tickDelta >= 1) {
                tickDelta--;
                gameLoop();
                ticks++;
            }

            if (userInputDelta >= 1) {
                userInputDelta--;
                UserCommands command = keyboard.getCommand();
                if (command == UserCommands.DROP) {
                    if (!dropping) {
                        nanosPerTick = 1000 * 1000 * 1000 / dropTPS;
                        dropping = true;
                    }
                } else {
                    if (dropping) {
                        nanosPerTick = 1000 * 1000 * 1000 / defaultTPS;
                        dropping = false;
                    }
                    game.userInput(command);
                }
            }

            if (timer > 1000000000) {
                //System.out.println("Frames per second: " + frames + ", ticks per second: " + ticks);
                frames = 0;
                timer = 0;
                ticks = 0;
            }
        }
    }

    private void start() {
        if (running) return;

        running = true;
        new Thread(this).start();
    }

    private void drawBoard() {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(5, 5, 5, HEIGHT - 5);
        graphics.drawLine(5, 5, WIDTH - 5, 5);
        graphics.drawLine(WIDTH - 5, HEIGHT - 5, WIDTH - 5, 5);
        graphics.drawLine(WIDTH - 5, HEIGHT - 5, 5, HEIGHT - 5);

        graphics.fillRect(boardLeft, boardTop, boardWidth, boardHeight);

        drawCells();

        graphics.setColor(Color.RED);
        for (int i = 1; i < Board.ROWS; i++)
            graphics.drawLine(boardLeft, boardBottom - i * cellSize, boardRight, boardBottom - i * cellSize);
        for (int i = 1; i < Board.COLUMNS; i++)
            graphics.drawLine(boardLeft + i * cellSize, boardTop, boardLeft + i * cellSize, boardBottom);
    }

    private void drawPreview() {
        graphics.setColor(Color.BLUE);
        graphics.fillRect(previewLeft, previewTop, previewWidth, previewHeight);

        Piece piece = game.getNextPiece();
        if (piece != null) {
            for (Point point : piece.getPoints()) {
                graphics.setColor(pieceColors.get(point.getColor() % pieceColors.size()));
                graphics.fillRect(
                        previewLeft + (point.getX() + 2) * cellSize,
                        previewTop - (point.getY() - 1) * cellSize,
                        cellSize,
                        cellSize);
            }
        }

        graphics.setColor(Color.RED);
        for (int i = 1; i < 4; i++)
            graphics.drawLine(previewLeft, previewBottom - i * cellSize, previewRight, previewBottom - i * cellSize);
        for (int i = 1; i < 4; i++)
            graphics.drawLine(previewLeft + i * cellSize, previewTop, previewLeft + i * cellSize, previewBottom);
    }

    private void drawCells() {
        Cell[][] cells = game.getBoard().getCells();
        Color color;
        Point point;
        for (int i = 0; i < Board.ROWS; i++)
            for (int j = 0; j < Board.COLUMNS; j++) {
                point = cells[i][j].getPoint();
                if (point != null) {
                    color = pieceColors.get(point.getColor() % pieceColors.size());
                    if (color == null) color = Color.GRAY;
                    graphics.setColor(color);
                    graphics.fillRect(boardLeft + j * cellSize, boardBottom - (i + 1) * cellSize, cellSize, cellSize);
                }
            }
    }

    private void drawStats() {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(statsLeft, statsTop, statsRight, statsTop);
        graphics.drawLine(statsLeft, statsBottom, statsRight, statsBottom);
        graphics.drawLine(statsLeft, statsTop, statsLeft, statsBottom);
        graphics.drawLine(statsRight, statsTop, statsRight, statsBottom);

        graphics.drawString("Scores: " + game.getScores(), statsLeft, statsTop + 25);
        graphics.drawString("Lines: " + game.getLinesBurnt(), statsLeft, statsTop + 55);
    }

    private void initBoardParams() {
        boardLeft = WIDTH/2 + 5;
        boardRight = WIDTH - 5;
        boardWidth = boardRight - boardLeft;
        boardHeight = Board.ROWS / Board.COLUMNS * boardWidth;
        cellSize = boardWidth / Board.COLUMNS;
        boardBottom = HEIGHT - 10;
        boardTop = boardBottom - boardHeight;
    }

    private void initPreviewParams() {
        previewTop = boardTop;
        previewBottom = previewTop + 4 * cellSize;
        previewRight = boardLeft - cellSize;
        previewLeft = previewRight - 4 * cellSize;
        previewHeight = 4 * cellSize;
        previewWidth = previewHeight;
    }

    private void initStatParams() {
        statsTop = (HEIGHT / 2) + 5;
        statsBottom = statsTop + (HEIGHT / 4);
        statsLeft = 5;
        statsRight = (WIDTH/2) - 5;
        statsWidth = statsRight - statsLeft;
        statsHeight = statsBottom - statsTop;
    }

    private void prepareColorList() {
        pieceColors.add(Color.GRAY);
        pieceColors.add(Color.GREEN);
        pieceColors.add(Color.YELLOW);
        pieceColors.add(Color.MAGENTA);
        pieceColors.add(Color.CYAN);
    }

    private void startGame() {
        game = new TetrisGame();
        game.start();
    }

}
