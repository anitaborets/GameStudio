package puzzle;

import java.util.Random;

public class FieldInit {
    static void fillArraysByRandomNumbers(int[][] grid, String[][] displayGrid) {

        //base init
        int i = 0;
        for (int row = 0; row <= grid.length - 1; row++) {
            for (int col = 0; col <= grid[0].length - 1; col++) {
                grid[row][col] = i;
                displayGrid[row + 1][col + 1] = Integer.toString(i);
                i++;
            }
        }

        //random shuffle
        do {
            initRandomTiles(grid);
        }
        while (isSolved(grid));


        //output
        for (int row = 0; row <= grid.length - 1; row++) {
            for (int col = 0; col <= grid[0].length - 1; col++) {
                displayGrid[row + 1][col + 1] = Integer.toString(grid[row][col]);
            }
        }
    }

    private static void initRandomTiles(int[][] grid) {
        int rndRowFrom;
        int rndRowDest;
        int rndColDest;
        int rndColFrom;
        Random random = new Random();

        for (int replacementsNumber = 0; replacementsNumber < 15; replacementsNumber++) {
            rndRowFrom = random.nextInt(grid.length);
            rndColFrom = random.nextInt(grid.length);
            rndRowDest = random.nextInt(grid.length);
            rndColDest = random.nextInt(grid.length);
            int temp = grid[rndRowFrom][rndColFrom];
            grid[rndRowFrom][rndColFrom] = grid[rndRowDest][rndColDest];
            grid[rndRowDest][rndColDest] = temp;
        }
    }

    //if game is solvable - with algoritm
    private static boolean isSolved(int[][] grid) {
        int count = 0;
        for (int row = 1; row <= grid.length - 1; row++) {
            for (int col = 1; col <= grid[0].length - 1; col++) {
                if (grid[col][row] > grid[col][row - 1]) {
                    count++;
                }
            }
        }
        return count % 2 == 0;
    }
}
