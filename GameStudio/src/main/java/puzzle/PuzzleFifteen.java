package puzzle;

import entity.Comment;
import entity.Rating;
import entity.Score;
import exceptions.CommentException;
import exceptions.RatingException;
import exceptions.ScoreException;

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

//    v triede sa ti miesa logika s vypismi do konzoly, preto je ten kod tazsie citatelny a pochopitelny
    public PuzzleFifteen() throws IOException {
   // public static void main(String[] args) throws IOException {
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
        dialog.append("<MOVE> - Choice to move <A1>") //vo 15puzzle by mal byt move pomocou wsad, pozri opravenu branchu v projekte Risa Andrejku
                .append("\n")
                .append("<EXIT> - EXIT").append("\n")
                .append("<SAVE> - SAVE AND EXIT").append("\n")
                .append("<NEW> - START NEW GAME").append("\n")
                .append("<com> - input comment and exit").append("\n")
                .append("<rat> - input rating of this game and exit");
    }

git a//    toto je strasne dlha metoda, treba to rozdelit na male podmetody, kazda urcena na jeden typ akcie
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


//        na toto by som asi osobitnu triedu nerobila, bud z toho spravit len metodu, alebo si drzat
//        pozicie prazdnej tile v tejto triede
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
                    System.out.println("rating " + ratingService.getAverageRating(PUZZLE_FIFTEEN));
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
        try {
//            vacsinou je efektivnejsie nechat databazu vyfiltrovat data, ako cez Javu
            List<Score> scoreList = scoreService.getBestScores(PUZZLE_FIFTEEN);
            //podmienku netreba, lebo ak bude empty, tak sa proste nevypise nic
            scoreList.forEach(System.out::println);
        } catch(ScoreException e) {
            System.out.println("Couldn't get scores, check database connection.");
        }
    }

    public static void setScore() {
        try {
            Score score = new Score(
                    userName, PUZZLE_FIFTEEN,
                    getPlayingSeconds(startMillis),
                    Timestamp.valueOf(LocalDateTime.now()));
            scoreService.insertScore(score);
        } catch(ScoreException e) {
            System.out.println("Couldn't save your score, check database connection.");
        }
    }

    private static void inputComment() throws CommentException {
        System.out.println("Input comment");
        String input;
        input = readLine();
        List<Comment> comments = null;

        try {
            Comment com = new Comment(
                    userName, PUZZLE_FIFTEEN,
                    input, Timestamp.valueOf(LocalDateTime.now()));
            commentService.addComment(com);
        } catch (CommentException e) {
            System.out.println(dialog);
        }

        try {
            comments = commentService.getComments(PUZZLE_FIFTEEN);
//        !!!nevyuzivat asserty na logicke podmienky v kode - su iba na testovanie
//        assert comments != null;
            for (Comment comment : comments) {
                System.out.println(comment);
            }
        } catch (CommentException e) {
            System.out.println(dialog);
        }
    }

    private static void setRating() throws RatingException {
        System.out.println("Input rating for " + PUZZLE_FIFTEEN + ": number from 0 to 5");
        int input;
        input = Integer.parseInt(Objects.requireNonNull(readLine()));

        if (input >= 0 && input <= 5) {
            Rating tempRating = new Rating(
                    userName, PUZZLE_FIFTEEN, input,
                    Timestamp.valueOf(LocalDateTime.now()));
            ratingService.setRating(tempRating);
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













