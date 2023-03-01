package minesweeper;

import minesweeper.consoleui.ConsoleUI;
import minesweeper.core.Field;
import minesweeper.entity.Rating;
import minesweeper.exceptions.RatingException;
import minesweeper.exceptions.WrongFormatException;
import minesweeper.service.CommentServiceJDBS;
import minesweeper.service.RatingServiceJDBC;
import minesweeper.service.ScoreServiceJDBC;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

/**
 * Main application class.
 */
public class Minesweeper {
    Properties properties = System.getProperties();
    /**
     * User interface.
     */
    private ConsoleUI userInterface;
    public static String userName;
    public static final ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
    public static final CommentServiceJDBS commentService = new CommentServiceJDBS();

    public static final RatingServiceJDBC rating = new RatingServiceJDBC();

    /**
     * Constructor.
     */
    private Minesweeper() throws WrongFormatException, SQLException {

        userInterface = new ConsoleUI();

        Field field = new Field(9, 9, 10);
        userName = System.getProperty("user.name");

        System.out.println(userName + ", WELCOME TO MINESWEEPER");
        System.out.println("Input your mail please if you want");
        try {
            inputEmail();
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
            return;
        }
        userInterface.newGameStarted(field);
    }

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) throws WrongFormatException, SQLException, RatingException {
        scoreService.createScoreTable();
        commentService.createCommentTable();
        rating.createRatingTable();
        scoreService.getBestScores();
       // System.out.println(userName);
        Rating rating1 = new Rating();
        rating1.setPlayer("userName");
        rating1.setRating(2);
        rating1.setGame("Minesweeper");
        rating1.setDate(Timestamp.valueOf(LocalDateTime.now()));
        rating.setRating(rating1);
        new Minesweeper();

    }

    //TODO prerobit
    private void inputEmail() throws WrongFormatException {
        Scanner input = new Scanner(System.in);
        String inputUserMail = input.nextLine();
        if (!isNull(inputUserMail) && !inputUserMail.isEmpty()) {
            Pattern pattern = Pattern.compile("^(.+)@(\\S+)$");
            Matcher matcher = pattern.matcher(inputUserMail);

            if (!matcher.find()) {
                throw new WrongFormatException("Incorrect format");
            }

            properties.setProperty("user.email", inputUserMail);
        }
    }
}
