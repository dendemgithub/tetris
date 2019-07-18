package com.tetris.gui;

import javax.swing.*;
import java.awt.*;

public class Tetris extends Canvas implements Runnable{

    private static final int WIDTH = 160;
    private static final int HEIGHT = WIDTH * 9 / 16;
    private static final int SCALE = 3;
    private static final String NAME = "Board";

    private JFrame frame;
    private boolean running = false;

    public Tetris() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //new Board().start();
    }

    public void start() {
        running = true;
        new Thread(this).run();
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000 * 1000 * 1000 / 60.0;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime)/nsPerTick;
            lastTime = now;
            boolean shouldRender = false;

            while (delta >= 1) {
                ticks++;
                tick();
                delta--;
                shouldRender = true;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println(ticks + " " + frames);
                frames = 0;
                ticks = 0;
            }
        }

    }

    private void render() {
        System.out.println("rendering");
    }

    private void tick() {}

}
