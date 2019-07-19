package com.tetris.model;

import java.util.ArrayList;
import java.util.Scanner;

public class TextClient {
    public static void main(String... args) {
        Scanner commands = new Scanner(System.in);
        String commandList;
        TetrisGame game = new TetrisGame();
        game.start();

        while(true) {
            System.out.println("Command: ");
            commandList = commands.nextLine();
            if (commandList == null || commandList.equals("exit")) break;
            for(char command: commandList.toCharArray()) {
                if (command == ' ') game.tick();
                if (command == 'a') game.userInput(UserCommands.LEFT);
                if (command == 'd') game.userInput(UserCommands.RIGHT);
                if (command == 's') game.userInput(UserCommands.ROTATE);
            }
            System.out.println(game.toString());
        }
    }
}
