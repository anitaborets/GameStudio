package puzzle;


public class Position {

    private int rowBlank;
    private int colBlank;
    private int[][] grid;
    private String[][] displayGrid;

    public Position(int rowBlank, int colBlank, int[][] grid, String[]... displayGrid) {
        this.rowBlank = rowBlank;
        this.colBlank = colBlank;
        this.grid = grid;
        this.displayGrid = displayGrid;
    }

    public int getRowBlank() {
        return rowBlank;
    }

    public int getColBlank() {
        return colBlank;
    }

    public Position zeroOutput() {

        for (int row = 0; row <= grid.length - 1; row++) {
            for (int col = 0; col <= grid.length - 1; col++) {
                if (grid[row][col] == 0) {
                    rowBlank = row;
                    colBlank = col;
                    displayGrid[rowBlank + 1][colBlank + 1] = " ";
                    break;
                }
            }
        }
        return this;
    }
}

