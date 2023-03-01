package minesweeper.core;

import java.util.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field {
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Clue && ((Clue) tile).getValue() == 0) {
            }

            if (tile instanceof Mine) {
                tile.setState(Tile.State.OPEN);
                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < columnCount; j++) {
                        openTile(i, j);
                    }
                }
                state = GameState.FAILED;
                return;
            }

            if (isSolved()) {
                state = GameState.SOLVED;
                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < columnCount; j++) {
                        openTile(i, j);
                    }
                }                return;

            }
        }
    }

    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.MARKED);
            return;
        }
        if (tile.getState() == Tile.State.MARKED) {
            tile.setState(Tile.State.CLOSED);
            return;
        }
    }

    public int getRemainingMineCount() {
        return getMineCount() - getNumberOf(Tile.State.MARKED);
    }

    /**
     * Generates playing field.
     */
    private void generate() {
        Random random = new Random();
        int count;
        if (mineCount < rowCount * columnCount) {
            count = mineCount;
        } else {
            return;
        }


        while (count > 0) {
            int x = random.nextInt(rowCount);
            int y = random.nextInt(columnCount);
            if (tiles[x][y] == null) {
                tiles[x][y] = new Mine();
                count--;
            }
        }

        //pocet min v susednych dlazdiciac
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j] == null) {
                    tiles[i][j] = new Clue(countAdjacentMines(i, j));
                }
            }
        }
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    public boolean isSolved() {
        return ((rowCount * columnCount) - getNumberOf(Tile.State.OPEN) == mineCount);
    }

    private int getNumberOf(Tile.State state) {
        int count = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j].getState() == state) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private void openAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        openTile(actRow, actColumn);
                    }
                }
            }
        }
    }

    private boolean outOfField(int nextRow, int nextColumn) {
        return nextRow < 0 || nextColumn < 0 || nextRow >= rowCount || nextColumn >= columnCount;
    }


}
