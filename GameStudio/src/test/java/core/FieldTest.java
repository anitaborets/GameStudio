package core;

import minesweeper.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static minesweeper.core.GameState.PLAYING;
import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    static final int ROWS = 9;
    static final int COLUMNS = 9;
    static final int MINES = 10;
    static final Field field = new Field(ROWS, COLUMNS, MINES);
    static final Tile[][] tiles = new Tile[ROWS][COLUMNS];
    private final Random randomGenerator = new Random();

    //TODO generate test + init new field
    @BeforeEach
    void initAll() {
        Random random = new Random();
        int count = MINES;
        while (count > 0) {
            int x = random.nextInt(ROWS);
            int y = random.nextInt(COLUMNS);
            if (tiles[x][y] == null) {
                tiles[x][y] = new Mine();
                count--;
            }
        }
    }

    @Test
    void setUp() {
        assertEquals(PLAYING, field.getState());
    }

    @Test
    void getRowCount() {
        assertEquals(ROWS, field.getRowCount());
    }

    @Test
    void getColumnCount() {
        assertEquals(COLUMNS, field.getColumnCount());
    }

    @Test
    void getMineCount() {
        assertEquals(MINES, field.getMineCount());
    }

    @Test
    void getState() {
        assertEquals(PLAYING, field.getState());
    }

    @Test
    void openTileIfClosed() {
        Tile tile = field.getTile(1, 1);
      //  tile.setState(Tile.State.CLOSED);
        field.openTile(1, 1);
        assertEquals(Tile.State.OPEN, tile.getState());
    }

    @Test
    void openTileIfMine() {
        Tile tile = field.getTile(1, 1);
      //  tile.setState(Tile.State.CLOSED);
        tile = new Mine();
        field.openTile(1, 1);
        assertEquals(GameState.FAILED, field.getState());
    }

    @Test
    void markTileIfClosed() {
        Tile tile = field.getTile(1, 1);
       // tile.setState(Tile.State.CLOSED);

        field.markTile(1, 1);
        assertEquals(Tile.State.MARKED, tile.getState());
    }

    @Test
    void markTileIfMarked() {
        Tile tile = field.getTile(1, 1);
        //tile.setState(Tile.State.MARKED);

        field.markTile(1, 1);
        assertEquals(Tile.State.CLOSED, tile.getState());
    }

    @Test
    void getRemainingMineCount() {
    }

    @Test
    public void isNotNull() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                assertNotNull(field.getTile(row, column));
            }
        }
    }

    @Test
    public void clueCount() {
        int clueCount = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if (field.getTile(row, column) instanceof Clue) {
                    clueCount++;
                }
            }
        }
        assertEquals(ROWS * COLUMNS - MINES, clueCount);
    }

    @Test
    public void isSolved() {
        assertEquals(PLAYING, field.getState());
        int open = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if (tiles[row][column] instanceof Clue) {
                    field.openTile(row, column);
                    open++;
                }
                if (field.getRowCount() * field.getColumnCount() - open == field.getMineCount()) {
                    assertEquals(GameState.SOLVED, field.getState());
                } else {
                    assertNotSame(GameState.FAILED, field.getState());
                }
            }
        }
        //        assertEquals(GameState.SOLVED, field.getState());
    }

    @Test
    public void checkMinesCount() {
        int minesCounter = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if (tiles[row][column] instanceof Mine) {
                    minesCounter++;
                }
            }
        }
        assertEquals(MINES, minesCounter);
    }

    @Test
    public void fieldWithTooManyMines() {
        Field fieldWithTooManyMines = null;
        int higherMineCount = ROWS * COLUMNS + randomGenerator.nextInt(10) + 1;
        try {
            fieldWithTooManyMines = new Field(ROWS, COLUMNS, higherMineCount);
        } catch (Exception e) {
            //field with more mines than tiles should not be created - it may fail on exception
        }
        assertFalse((fieldWithTooManyMines == null) || (fieldWithTooManyMines.getMineCount() <= (ROWS * COLUMNS)));
    }
}