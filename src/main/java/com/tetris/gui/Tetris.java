package com.tetris.gui;

import com.tetris.keyboard.KeyboardListner;
import com.tetris.model.*;
import com.tetris.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

public class Tetris extends Canvas implements Runnable {
    private static final String name = "Tetris";
    private static final int HEIGHT = 800;
    private static final int WIDTH = 600;

    private int FPS = 60;
    private int defaultTPS = 3; // see below
    private int dropTPS = 30; // defaultTPS for "drop mode"
    private boolean droping = false;
    private double nanosPerTick =  1000 * 1000 * 1000 / defaultTPS;
    private int CPS = 20; // how frequently user input is processed

    private int boardHeigth;
    private int boardWidth;
    private int cellSize;
    private int boardTop;
    private int boardBottom;
    private int boardLeft;
    private int boardRight;
    private Map<Integer, Color> colorMap;


    private boolean running;
    private TetrisGame game;

    private BufferStrategy strategy;
    private Graphics2D graphics;
    private KeyboardListner keyboard = new KeyboardListner();


    public Tetris() {
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

        calculateBoardParams();

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
        strategy.show();
        graphics.dispose();
    }


    @Override
    public void run() {
        long now;
        long lastTime = System.nanoTime();
        double nanosPerFrame = 1000 * 1000 * 1000/FPS;
        double nanosPerCommand =  1000 * 1000 * 1000/CPS;
        double frameDelta = 0;
        double tickDelta = 0;
        double userInputDelta = 0;

        long timer = 0;
        int frames = 0;
        int ticks = 0;

        while (running) {
            now = System.nanoTime();
            frameDelta += (now - lastTime)/nanosPerFrame;
            tickDelta += (now - lastTime)/nanosPerTick;
            userInputDelta += (now - lastTime)/nanosPerCommand;

            timer += now - lastTime;
            lastTime = now;

            if(frameDelta >= 1) {
                frameDelta--;
                render();
                frames++;
            }

            if(tickDelta >= 1) {
                tickDelta--;
                gameLoop();
                ticks++;
            }

            if(userInputDelta >= 1) {
                userInputDelta--;
                UserCommands command = keyboard.getCommand();
                if(command == UserCommands.DROP) {
                    if(!droping) {
                        nanosPerTick =  1000 * 1000 * 1000 / dropTPS;
                        droping = true;
                    }
                } else {
                    if(droping) {
                        nanosPerTick =  1000 * 1000 * 1000 / defaultTPS;
                        droping = false;
                    }
                    game.userInput(command);
                }
            }

            if(timer > 1000000000) {
                System.out.println("Frames per second: " + frames + ", ticks per second: " + ticks);
                frames = 0;
                timer = 0;
                ticks = 0;
            }
        }
    }

    private void start() {
        if(running) return;

        running = true;
        new Thread(this).start();
    }

    private void drawBoard() {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(5, 5, 5, HEIGHT-5);
        graphics.drawLine(5, 5, WIDTH-5, 5);
        graphics.drawLine(WIDTH-5, HEIGHT-5, WIDTH-5, 5);
        graphics.drawLine(WIDTH-5, HEIGHT-5, 5, HEIGHT-5);

        graphics.setColor(Color.BLUE);
        graphics.fillRect(boardLeft, boardTop, boardWidth, boardHeigth);

        drawCells();

        graphics.setColor(Color.RED);
        for(int i = 1; i < Board.ROWS; i++)
            graphics.drawLine(boardLeft, boardBottom - i*cellSize, boardRight, boardBottom - i*cellSize);
        for(int i = 1; i < Board.COLUMNS; i++)
            graphics.drawLine(boardLeft + i*cellSize, boardTop, boardLeft + i*cellSize, boardBottom);
    }

    private void drawCells() {
        Cell[][] cells = game.getBoard().getCells();
        Color color;
        Point point;
        for(int i = 0; i < Board.ROWS; i++)
            for (int j = 0; j < Board.COLUMNS; j++) {
                point = cells[i][j].getPoint();
                if (point != null) {
                    color = colorMap.get(cells[i][j].getPoint().getColor());
                    if (color == null) color = Color.GRAY;
                    graphics.setColor(Color.GRAY);
                    graphics.fillRect(boardLeft + j * cellSize, boardBottom - (i + 1) * cellSize, cellSize, cellSize );
                }
            }
    }

    private void calculateBoardParams() {
        boardWidth = WIDTH * 2 / 4;
        boardHeigth = Board.ROWS / Board.COLUMNS * boardWidth;
        cellSize = boardWidth / Board.COLUMNS;
        boardBottom = HEIGHT - 10;
        boardTop = boardBottom - boardHeigth;
        boardRight = WIDTH - 10;
        boardLeft = boardRight - boardWidth;
        colorMap = new HashMap<>();
        colorMap.put(0, Color.GRAY);

    }

    private void startGame() {
        game = new TetrisGame();
        game.start();
    }
}
