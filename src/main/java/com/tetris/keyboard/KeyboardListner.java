package com.tetris.keyboard;

import com.tetris.model.UserCommands;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListner implements KeyListener {
    UserCommands command;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                command = UserCommands.START;
                break;
            case KeyEvent.VK_P:
                command = UserCommands.PAUSE;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                command = UserCommands.RIGHT;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                command = UserCommands.LEFT;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_W:
                command = UserCommands.ROTATE;
                break;
            case KeyEvent.VK_SPACE:
                command = UserCommands.DROP;
                break;
            //cheats
            case KeyEvent.VK_CONTROL:
                command = UserCommands.CHEAT_ROTATE_NEXT;
                break;
        }
    }

    public UserCommands getCommand() {
        UserCommands command = this.command;
        this.command = null;
        return command;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
