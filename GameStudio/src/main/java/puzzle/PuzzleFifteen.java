package puzzle;

import entity.Comment;
import entity.Rating;
import entity.Score;
import exceptions.CommentException;
import exceptions.RatingException;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static entity.Constants.PUZZLE_FIFTEEN;
import static entity.Constants.WIN;
import static minesweeper.Minesweeper.*;
import static minesweeper.Minesweeper.commentService;
import static service.ScoreServiceJDBC.getPlayingSeconds;

public class PuzzleFifteen implements Externalizable {
    static final long serialVersionUID = 1L;
    private static long startMillis;
    private static final StringBuilder dialog = new StringBuilder();
    private static final int rows = 4;
    private static final int cols = 4;
    static int[][] grid = new int[rows][cols];
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static final String SAVED_GAME = System.getProperty("user.home") + System.getProperty("file.separator") + "saved.game";
    static Logger LOGGER = Logger.getLogger(PuzzleFifteen.class.getName());
    static Scanner choiceScanner = new Scanner(System.in);
    static String choice;

    public static void main(String[] args) throws IOException {

        int rowBlank = 0;
        int colBlank = 0;

        int move = 1;
        boolean correctChoice = true;

        String[][] displayGrid = {{" *", "A", "B", "C", "D"},
                {" 1|", "null", "null", "null", "null"},
                {" 2|", "null", "null", "null", "null"},
                {" 3|", "null", "null", "null", "null"},
                {" 4|", "null", "null", "null", "null"}};
        int[][] wonGrid = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};

        userName = System.getProperty("user.name");

        startDialog();

        gameStart(rowBlank, colBlank, move, correctChoice, grid, displayGrid, wonGrid);
    }

    private static void startDialog() {
        dialog.append("<MOVE> - Choice to move <A1>")
                .append("\n")
                .append("<EXIT> - EXIT").append("\n")
                .append("<SAVE> - SAVE AND EXIT").append("\n")
                .append("<NEW> - START NEW GAME").append("\n")
                .append("<com> - input comment and exit").append("\n")
                .append("<rat> - input rating of this game and exit");
    }

    private static void gameStart(int rowBlank, int colBlank, int move, boolean correctChoice, int[][] grid, String[][] displayGrid, int[][] wonGrid) throws IOException {
        char rowChoiceChar;
        int rowChoice;
        int colChoice;
        char colChoiceChar;

        System.out.println("Welcome to " + PUZZLE_FIFTEEN);
        getScore();
        System.out.println("**********************");
        System.out.println("Do you want to load last game - Y/N?");
        choice = choiceScanner.next();

        if (choice.equalsIgnoreCase("Y")) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVED_GAME));
            try {
                grid = (int[][]) in.readObject();
                for (int row = 0; row <= grid.length - 1; row++) {
                    for (int col = 0; col <= grid[0].length - 1; col++) {
                        displayGrid[row + 1][col + 1] = Integer.toString(grid[row][col]);
                    }
                }
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                System.out.println("Saved game not exists");
                FieldInit.fillArraysByRandomNumbers(grid, displayGrid);
            }
        } else {
            FieldInit.fillArraysByRandomNumbers(grid, displayGrid);
        }

        Position position = new Position(rowBlank, colBlank, grid, displayGrid).zeroOutput();
        rowBlank = position.getRowBlank();
        colBlank = position.getColBlank();

        startMillis = System.currentTimeMillis();

        while (!(Arrays.deepEquals(grid, wonGrid))) {

            if (correctChoice) {
                System.out.println("Counts of move: " + move);
                System.out.println("Time in sec: " + (System.currentTimeMillis() - startMillis) / 1000);
                for (int col = 0; col <= displayGrid[0].length - 1; col++) {
                    System.out.print(displayGrid[0][col] + "\t");
                }
                System.out.println("\n");
                System.out.println("--------------------");

                for (int row = 1; row <= displayGrid.length - 1; row++) {
                    System.out.print(displayGrid[row][0] + "\t");
                    for (int col = 1; col <= displayGrid[0].length - 1; col++) {
                        System.out.print(displayGrid[row][col] + "\t");
                    }
                    System.out.println("|\n");
                }
            }

            System.out.println(dialog);

            choice = choiceScanner.next();

            if (choice.equalsIgnoreCase("EXIT")) {
                setScore();
                System.out.println("GOOD BY!");
                System.exit(0);
            }

            if (choice.equalsIgnoreCase("com")) {
                try {
                    inputComment();
                    return;
                } catch (CommentException e) {
                    return;
                }
            }
            if (choice.equalsIgnoreCase("rat")) {
                try {
                    setRating();
                    System.out.println("rating " + rating.getAverageRating(PUZZLE_FIFTEEN));
                    return;
                } catch (RatingException e) {
                    return;
                }
            }

            if (choice.equalsIgnoreCase("new")) {
                gameStart(rowBlank, colBlank, move, correctChoice, grid, displayGrid, wonGrid);

            }

            if (choice.equalsIgnoreCase("SAVE")) {
                FileOutputStream fileOutputStream = new FileOutputStream(SAVED_GAME);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(grid);
                System.exit(0);
            }

            colChoiceChar = choice.charAt(0);

            InputValidate inputValidate = new InputValidate(colChoiceChar).invoke();
            if (inputValidate.is()) continue;
            colChoice = inputValidate.getColChoice();
            rowChoiceChar = choice.charAt(1);
            rowChoice = Character.getNumericValue(rowChoiceChar) - 1;

            if (rowChoice != rowBlank - 1 && rowChoice != rowBlank + 1 && rowChoice != rowBlank || colChoice != colBlank - 1 && colChoice != colBlank + 1 && colChoice != colBlank) {
                System.out.println("  You can move only numbers\n  which are next to the blank spot.");
                correctChoice = false;
                move--;
                continue;
            }

            Replacement replacement = new Replacement(rowChoice, colChoice, rowBlank, colBlank, grid, displayGrid).invoke();
            rowBlank = replacement.getRowBlank();
            colBlank = replacement.getColBlank();
            correctChoice = true;
            move++;
        }

        System.out.println(WIN);
        setScore();
        getScore();
    }

    public static void getScore() {
        List<Score> scoreList = scoreService.getBestScores();
        List<Score> scoreListPuzzle = scoreList.stream().filter(score -> score.getGame().equals(PUZZLE_FIFTEEN)).toList();
        if (!scoreListPuzzle.isEmpty()) {
            for (Score sc : scoreListPuzzle) {
                System.out.println(sc);
            }
        }
    }

    public static void setScore() {
        Score score = new Score();
        score.setPlayer(userName);
        score.setGame(PUZZLE_FIFTEEN);
        score.setScore(getPlayingSeconds(startMillis));
        score.setPlayedOn(Timestamp.valueOf(LocalDateTime.now()));
        scoreService.insertScore(score);
    }

    private static void inputComment() throws CommentException {
        System.out.println("Input comment");
        String input;
        input = readLine();
        List<Comment> comments = null;

        Comment com = new Comment();
        com.setPlayer(userName);
        com.setGame(PUZZLE_FIFTEEN);
        com.setComment(input);
        com.setCommentedOn(Timestamp.valueOf(LocalDateTime.now()));
        try {
            commentService.addComment(com);
        } catch (CommentException e) {
            System.out.println(dialog);
        }
        try {
            comments = commentService.getComments(PUZZLE_FIFTEEN);
        } catch (CommentException e) {
            System.out.println(dialog);
        }

        assert comments != null;
        for (Comment comment : comments) {
            System.out.println(comment);
        }
    }

    private static void setRating() throws RatingException {
        System.out.println("Input rating for " + PUZZLE_FIFTEEN + ": number from 0 to 5");
        int input;
        input = Integer.parseInt(Objects.requireNonNull(readLine()));

        if (input >= 0 && input <= 5) {
            Rating tempRating = new Rating();
            tempRating.setPlayer(userName);
            tempRating.setRating(input);
            tempRating.setGame(PUZZLE_FIFTEEN);
            tempRating.setRatedOn(Timestamp.valueOf(LocalDateTime.now()));
            rating.setRating(tempRating);
        } else {
            System.out.println("Incorrect format");
            System.out.println(dialog);
        }
    }

    private static String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(grid);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        grid = (int[][]) in.readObject();

    }

}













