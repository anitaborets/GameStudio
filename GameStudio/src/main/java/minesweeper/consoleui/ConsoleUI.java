package minesweeper.consoleui;

import minesweeper.core.Field;
import minesweeper.core.Tile;
import minesweeper.entity.Score;
import minesweeper.exceptions.WrongFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static minesweeper.Minesweeper.scoreService;
import static minesweeper.Minesweeper.userName;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {
    private static final String GAME_OVER = "GAME OVER!";
    private static final String WIN = "YOU ARE WIN!";


    private Field field;

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Starts the game.
     *
     * @param field field of mines and clues
     */
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
                    inputScore();
                    System.exit(0);
                }
                case SOLVED -> {
                    System.out.println(WIN);
                    inputScore();


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
                .append("<OB4> - OPEN B4");
        System.out.println(builder);
    }

    @Override
    public void inputScore() {
        Score score = new Score();
        score.setPlayer(userName);
        //TODO
        score.setScore(1);
        score.setPlayedOn(Timestamp.valueOf(LocalDateTime.now()));
        scoreService.insertScore(score);
        List<Score> scoreList = scoreService.getBestScores();
        for (Score sc : scoreList) {
            System.out.println(sc);
        }
    }

    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
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
            Pattern pattern = Pattern.compile("([oOmM])([a-iA-I])([0-8])|[EXIT]|[exit]");
            Matcher matcher = pattern.matcher(input);

            if (!matcher.find()) {
                throw new WrongFormatException("Incorrect format");
            }
        }
    }


}
