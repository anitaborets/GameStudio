package minesweeper;

import minesweeper.consoleui.ConsoleUI;
import minesweeper.core.Field;
import exceptions.RatingException;
import exceptions.WrongFormatException;
import service.CommentServiceJDBS;
import service.RatingServiceJDBC;
import service.ScoreServiceJDBC;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static entity.Constants.MINESWEEPER;
import static minesweeper.Settings.*;

/**
 * Main application class.
 */
public class Minesweeper {
    Properties properties = System.getProperties();
    Settings settings;
    private ConsoleUI userInterface;
    public static String userName;
    public static long startMillis;
    public static final ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
    public static final CommentServiceJDBS commentService = new CommentServiceJDBS();
    public static final RatingServiceJDBC rating = new RatingServiceJDBC();
    Logger LOGGER = Logger.getLogger(Minesweeper.class.getName());

    /**
     * Constructor.
     */
    public Minesweeper() {

        userInterface = new ConsoleUI();
        System.out.println("Select level of game");
        StringBuilder builder = new StringBuilder();
        builder.append("Please enter your selection: ").append("\n")
                .append("<B> - BEGINNER").append("\n")
                .append("<I> -INTERMEDIATE").append("\n")
                .append("<E> - EXPERT").append("\n");
        System.out.println(builder);
        Scanner input = new Scanner(System.in);
        String choice = input.nextLine();

        switch (choice) {
            case "B" -> settings = BEGINNER;
            case "I" -> settings = INTERMEDIATE;
            case "E" -> settings = EXPERT;
            default -> settings = BEGINNER;
        }
        try {
            settings.save();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        try {
            settings = Settings.load();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            settings = BEGINNER;
        }


        Field field = new Field(settings.getRowCount(), settings.getColumnCount(), settings.getMineCount());
        userName = System.getProperty("user.name");

        System.out.println(userName + ", WELCOME TO MINESWEEPER");

        // System.out.println("Input your mail please if you want");
        //  try {
        //  inputEmail();
        //  } catch (WrongFormatException e) {
        //   System.out.println(e.getMessage());
        // return;
        //}
        userInterface.newGameStarted(field);
        startMillis = System.currentTimeMillis();
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * Main method.mk
     *
     * @param args arguments
     */
    public static void main(String[] args) throws WrongFormatException, SQLException, RatingException {
        scoreService.createScoreTable();
        commentService.createCommentTable();
        rating.createRatingTable();

        System.out.println("rating " + rating.getAverageRating(MINESWEEPER));

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
