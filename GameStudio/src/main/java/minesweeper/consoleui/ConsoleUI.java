package minesweeper.consoleui;

import exceptions.ScoreException;
import minesweeper.core.Field;
import minesweeper.core.Tile;
import entity.Comment;
import entity.Rating;
import entity.Score;
import exceptions.CommentException;
import exceptions.RatingException;
import exceptions.WrongFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static entity.Constants.*;
import static java.util.Objects.isNull;
import static minesweeper.Minesweeper.*;
import static service.ScoreServiceJDBC.getPlayingSeconds;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {
    private Field field;
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void newGameStarted(Field field) {
        this.field = field;
        do {
            update();
            processInput();
            switch (field.getState()) {
                case FAILED -> {
                    update();
                    System.out.println(GAME_OVER);
                    getScore();
                    System.exit(0);
                }
                case SOLVED -> {
                    System.out.println(WIN);
                    setScore();
                    getScore();
                }
            }
        } while (true);
    }

    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {
        Formatter formatter = new Formatter();
        String closedTile = "\u2618";

        System.out.print("  ");
        System.out.println("Pocet min: " + field.getMineCount());
        System.out.println("  Ty si mysliš, že ešte ostava min: " + (field.getRemainingMineCount()));

        System.out.print("   ");
        for (int i = 0; i < field.getRowCount(); i++) {
            System.out.printf("%4s", i);
        }
        System.out.print("\n");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < field.getRowCount(); i++) {
            Character c = (char) (i + 65);
            System.out.print(c + "| ");
            for (int j = 0; j < field.getColumnCount(); j++) {
                if (field.getTiles()[i][j].getState().equals(Tile.State.MARKED)) {
                    System.out.printf("%3s", "M");
                    continue;
                }
                if (field.getTiles()[i][j].getState().equals(Tile.State.CLOSED)) {
                    System.out.printf("%4s", closedTile);
                    continue;

                } else {
                    System.out.printf("%3s", field.getTiles()[i][j]);
                    continue;
                }
            }
            System.out.print("| ");
            System.out.print("\n");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Please enter your selection: ").append("\n")
                .append("<EXIT> - EXIT").append("\n")
                .append("<MA1> - MARK A1").append("\n")
                .append("<OB4> - OPEN B4").append("\n")
                .append("<com> - input comment").append("\n")
                .append("<rat> - input rating of this game");
        System.out.println(builder);
    }

    @Override
    public void getScore() {
        try {
            List<Score> scoreList = scoreService.getBestScores(MINESWEEPER);
            for (Score sc : scoreList) {
                System.out.println(sc);
            }
        } catch(ScoreException e) {
            System.out.println("Couldn't get scores, check database connection.");
        }
    }

    @Override
    public void setScore() {
        try {
            Score score = new Score(
                    userName, MINESWEEPER,
                    getPlayingSeconds(startMillis),
                    Timestamp.valueOf(LocalDateTime.now()));
            scoreService.insertScore(score);
        } catch(ScoreException e) {
            System.out.println("Couldn't save your score, check database connection.");
        }
    }


    private void processInput() {
        Scanner input = new Scanner(System.in);
        String choice = input.nextLine();

        try {
            handleInput(choice);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
            processInput();
        }

        if (choice.equalsIgnoreCase("EXIT")) {
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
                System.out.println("rating " + ratingService.getAverageRating(MINESWEEPER));
                return;
            } catch (RatingException e) {
                return;
            }
        }


        char[] symbols = choice.toUpperCase(Locale.ROOT).toCharArray();
        char state = Character.toUpperCase(symbols[0]);
        int row = Character.getNumericValue(symbols[1]) - 10;
        int column = Integer.parseInt(String.valueOf(symbols[2]));

        switch (state) {
            case 'O' -> field.openTile(row, column);
            case 'M' -> field.markTile(row, column);
        }
    }

    private static void handleInput(String input) throws WrongFormatException {
        if (!isNull(input) && !input.isEmpty()) {
            Pattern pattern = Pattern.compile("([oOmM])([a-iA-I])([0-8])|[EXIT]|[exit]|[com]");
            Matcher matcher = pattern.matcher(input);

            if (!matcher.find()) {
                throw new WrongFormatException("Incorrect format");
            }
        }
    }

    private void setRating() throws RatingException {
        System.out.println("Input rating for " + MINESWEEPER + ": number from 0 to 5");
        int input;
        input = Integer.parseInt(Objects.requireNonNull(readLine()));

        if (input >= 0 && input <= 5) {
            Rating tempRating = new Rating();
            tempRating.setPlayer(userName);
            tempRating.setRating(input);
            tempRating.setGame(MINESWEEPER);
            tempRating.setRatedOn(Timestamp.valueOf(LocalDateTime.now()));
            ratingService.setRating(tempRating);
        } else {
            System.out.println("Incorrect format");
            update();
        }
    }

    private void inputComment() throws CommentException {
        System.out.println("Input comment");
        String input;
        input = readLine();
        List<Comment> comments = null;

        Comment com = new Comment();
        com.setPlayer(userName);
        com.setGame(MINESWEEPER);
        com.setComment(input);
        com.setCommentedOn(Timestamp.valueOf(LocalDateTime.now()));
        try {
            commentService.addComment(com);
        } catch (CommentException e) {
            update();
        }
        try {
            comments = commentService.getComments(MINESWEEPER);
        } catch (CommentException e) {
            update();
        }

        assert comments != null;
        for (Comment comment : comments) {
            System.out.println(comment);
        }
    }
}


