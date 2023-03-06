import minesweeper.consoleui.ConsoleUI;
import exceptions.RatingException;
import exceptions.WrongFormatException;
import service.CommentServiceJDBS;
import service.RatingServiceJDBC;
import service.ScoreServiceJDBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

public class Gamestudio {
    Properties properties = System.getProperties();

    /**
     * User interface.
     */
    private ConsoleUI userInterface;
    public static String userName;
    private static long startMillis;
    public static final ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
    public static final CommentServiceJDBS commentService = new CommentServiceJDBS();
    public static final RatingServiceJDBC rating = new RatingServiceJDBC();
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }


    public static void main(String[] args) throws WrongFormatException, SQLException, RatingException {


    }
}
