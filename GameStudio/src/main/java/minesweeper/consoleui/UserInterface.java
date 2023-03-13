package minesweeper.consoleui;

import minesweeper.core.Field;

public interface UserInterface {
    void newGameStarted(Field field);

    void update();

    void getScore();

    void setScore();
}
