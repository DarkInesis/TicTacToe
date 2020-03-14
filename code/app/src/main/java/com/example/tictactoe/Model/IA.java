package com.example.tictactoe.Model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class IA extends Player {
    private boolean[][] actionsPlayed;
    private String difficulty;

    public IA(String difficulty) {
        super();
        this.difficulty = difficulty;
        this.actionsPlayed = new boolean[][]{{false, false, false}, {false, false, false}, {false, false, false}};
    }

    public int[] play() {
        switch (this.difficulty.toUpperCase()) {
            case "EASY": {
                return this.playEasy();
            }
            case "MEDIUM": {
                return this.playMedium();
            }
            case "HARD": {
                return this.playHard();
            }
        }
        return null;
    }

    private int[] playEasy() {
        int[][] gameMatrix = Game.getGame().getMyGameMatrix().getValue();
        Random rand = new Random();
        int line;
        int column;
        do {
            line = rand.nextInt(3);
            column = rand.nextInt(3);
        } while (gameMatrix[line][column] != 0);
        // Update of actionsPlayed
        this.actionsPlayed[line][column] = true;
        int[] position = {line, column};
        return position;
    }

    private int[] playMedium() {
        int[][] gameMatrix = Game.getGame().getMyGameMatrix().getValue();
        int[] blockPosition = this.positionForBlock();
        // If it's possible to win, play the move needed for win
        int[] winPosition = this.positionForWin();
        if (winPosition[0] != -1 && winPosition[1] != -1) {
            this.actionsPlayed[winPosition[0]][winPosition[1]] = true;
            return winPosition;
        }
        // If the adversaire can win, block him
        else if (blockPosition[0] != -1 && blockPosition[1] != -1) {
            this.actionsPlayed[blockPosition[0]][blockPosition[1]] = true;
            return blockPosition;
        }
        // If it's not possible to win : play center if possible Or play aleatory
        else if (gameMatrix[1][1] == 0) {
            this.actionsPlayed[1][1] = true;
            return new int[]{1, 1};
        } else {
            return this.playEasy();
        }
    }

    private int[] playHard() {
        //temp
        return this.playMedium();
    }

    private int[] positionForWin() {
        int[] positionForWin = new int[]{-1, -1};
        int[][] gameMatrix = Game.getGame().getMyGameMatrix().getValue();
        // Test line
        for (int line = 0; line < 3; line++) {
            if (this.actionsPlayed[line][0] && this.actionsPlayed[line][1] && gameMatrix[line][2] == 0) {
                positionForWin = new int[]{line, 2};
            } else if (this.actionsPlayed[line][0] && this.actionsPlayed[line][2] && gameMatrix[line][1] == 0) {
                positionForWin = new int[]{line, 1};
            } else if (this.actionsPlayed[line][1] && this.actionsPlayed[line][2] && gameMatrix[line][0] == 0) {
                positionForWin = new int[]{line, 0};
            }
        }
        // Test columns
        for (int column = 0; column < 3; column++) {
            if (this.actionsPlayed[0][column] && this.actionsPlayed[1][column] && gameMatrix[2][column] == 0) {
                positionForWin = new int[]{2, column};
            } else if (this.actionsPlayed[0][column] && this.actionsPlayed[2][column] && gameMatrix[1][column] == 0) {
                positionForWin = new int[]{1, column};
            } else if (this.actionsPlayed[1][column] && this.actionsPlayed[2][column] && gameMatrix[0][column] == 0) {
                positionForWin = new int[]{0, column};
            }
        }
        // Test diagonal left -> right
        if (this.actionsPlayed[1][1]) {
            if (this.actionsPlayed[0][0] && gameMatrix[2][2] == 0) {
                positionForWin = new int[]{2, 2};
            } else if (this.actionsPlayed[2][2] && gameMatrix[0][0] == 0) {
                positionForWin = new int[]{0, 0};
            }
        } else if (this.actionsPlayed[0][0] && this.actionsPlayed[2][2] && gameMatrix[1][1] == 0) {
            positionForWin = new int[]{1, 1};
        }
        // Test diagonal right -> left
        if (this.actionsPlayed[1][1]) {
            if (this.actionsPlayed[0][2] && gameMatrix[2][0] == 0) {
                positionForWin = new int[]{2, 0};
            } else if (this.actionsPlayed[2][0] && gameMatrix[0][2] == 0) {
                positionForWin = new int[]{0, 2};
            }
        } else if (this.actionsPlayed[0][2] && this.actionsPlayed[2][0] && gameMatrix[1][1] == 0) {
                positionForWin = new int[]{1, 1};
        }
        return positionForWin;
    }

    private int[] positionForBlock() {
        int[] positionForBlock = new int[]{-1, -1};
        int[][] gameMatrix = Game.getGame().getMyGameMatrix().getValue();
        // Initialization of the actions played by adversary
        boolean[][] actionsPlayedByAdversaire = new boolean[3][3];
        for (int line = 0; line < 3; line++) {
            boolean[] lineBool = new boolean[3];
            for (int column = 0; column < 3; column++) {
                lineBool[column] = gameMatrix[line][column] == 1;
            }
            actionsPlayedByAdversaire[line] = lineBool;
        }
        // Test line
        for (int line = 0; line < 3; line++) {
            if (actionsPlayedByAdversaire[line][0] && actionsPlayedByAdversaire[line][1] && gameMatrix[line][2] == 0) {
                positionForBlock = new int[]{line, 2};
            } else if (actionsPlayedByAdversaire[line][0] && actionsPlayedByAdversaire[line][2] && gameMatrix[line][1] == 0) {
                positionForBlock = new int[]{line, 1};
            } else if (actionsPlayedByAdversaire[line][1] && actionsPlayedByAdversaire[line][2] && gameMatrix[line][0] == 0) {
                positionForBlock = new int[]{line, 0};
            }
        }
        // Test columns
        for (int column = 0; column < 3; column++) {
            if (actionsPlayedByAdversaire[0][column] && actionsPlayedByAdversaire[1][column] && gameMatrix[2][column] == 0) {
                positionForBlock = new int[]{2, column};
            } else if (actionsPlayedByAdversaire[0][column] && actionsPlayedByAdversaire[2][column] && gameMatrix[1][column] == 0) {
                positionForBlock = new int[]{1, column};
            } else if (actionsPlayedByAdversaire[1][column] && actionsPlayedByAdversaire[2][column] && gameMatrix[0][column] == 0) {
                positionForBlock = new int[]{0, column};
            }
        }
        // Test diagonal left -> right
        if (actionsPlayedByAdversaire[1][1]) {
            if (actionsPlayedByAdversaire[0][0] && gameMatrix[2][2] == 0) {
                positionForBlock = new int[]{2, 2};
            } else if (actionsPlayedByAdversaire[2][2] && gameMatrix[0][0] == 0) {
                positionForBlock = new int[]{0, 0};
            }
        } else if (actionsPlayedByAdversaire[0][0] && actionsPlayedByAdversaire[2][2] && gameMatrix[1][1] == 0){
                positionForBlock = new int[]{1, 1};
        }
        // Test diagonal right -> left
        if (actionsPlayedByAdversaire[1][1]) {
            if (actionsPlayedByAdversaire[0][2] && gameMatrix[2][0] == 0) {
                positionForBlock = new int[]{2, 0};
            } else if (actionsPlayedByAdversaire[2][0] && gameMatrix[0][2] == 0) {
                positionForBlock = new int[]{0, 2};
            }
        } else if (actionsPlayedByAdversaire[0][2] && actionsPlayedByAdversaire[2][0] && gameMatrix[1][1] == 0) {
            positionForBlock = new int[]{1, 1};
        }
        return positionForBlock;
    }

    public boolean isWin() {
        // Case of the player tac the central case
        if (this.actionsPlayed[1][1]) {
            // Tests diagonals
            if ((this.actionsPlayed[0][0] && this.actionsPlayed[2][2]) || (this.actionsPlayed[0][2] && this.actionsPlayed[2][0])) {
                return true;
            }
        }
        boolean[] lineCompleted = {true, true, true};
        for (int line = 0; line < 3; line++) {
            if (this.actionsPlayed[line][0] && this.actionsPlayed[line][1] && this.actionsPlayed[line][2]) {
                return true;
            }
        }
        for (int column = 0; column < 3; column++) {
            // Test columns
            if (this.actionsPlayed[0][column] && this.actionsPlayed[1][column] && this.actionsPlayed[2][column]) {
                return true;
            }
        }
        return false;
    }
}
