package puzzle;

import minesweeper.core.Field;
import minesweeper.core.Tile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Random;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;
import static puzzle.FieldInit.*;

class FieldInitTest {

    static final int rows = 4;
    static final int col = 4;
    static final int[][] puzzleField = new int[rows][col];
    static final String[][] puzzleFieldToDisplay = new String[rows + 1][col + 1];
    static final int sumOfAllElements = 120;

    @Test
    void baseArrayInitTest() {
        int sum = 0;

        assertEquals(4, puzzleField.length);

        baseArrayInit(puzzleField, puzzleFieldToDisplay);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = sum + puzzleField[i][j];
            }
        }
        assertEquals(sumOfAllElements, sum);
    }


    @Test
    void fillArraysByRandomNumbers() {
    }

    @Test
    void displayInitTest() {
        assertEquals(5, puzzleFieldToDisplay.length);

    }


}