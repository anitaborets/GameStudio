import minesweeper.Minesweeper;
import minesweeper.consoleui.ConsoleUI;
import exceptions.RatingException;
import exceptions.WrongFormatException;
import puzzle.PuzzleFifteen;
import service.CommentServiceJDBS;
import service.RatingServiceJDBC;
import service.ScoreServiceJDBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import static minesweeper.Settings.*;
import static minesweeper.Settings.BEGINNER;


public class Gamestudio {
    Properties properties = System.getProperties();
    public static String userName;

    private ConsoleUI userInterface;

    private static long startMillis;
    public static final ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
    public static final CommentServiceJDBS commentService = new CommentServiceJDBS();
    public static final RatingServiceJDBC rating = new RatingServiceJDBC();
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private static String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }


    public static void main(String[] args) throws RatingException, IOException {
        Gamestudio gamestudio = new Gamestudio();
        StringBuilder builder = new StringBuilder();
        builder.append("Choice the game please: ").append("\n")
                .append("<M> - MINESWEEPER").append("\n")
                .append("<P> -PUZZLE-FIFTEEN").append("\n");
        System.out.println(builder);
        Scanner input = new Scanner(System.in);
        String choice = input.nextLine();

        switch (choice) {
            case "M" -> new Minesweeper();
            case "P" -> new PuzzleFifteen();
            default -> System.exit(0);
        }
    }
}
