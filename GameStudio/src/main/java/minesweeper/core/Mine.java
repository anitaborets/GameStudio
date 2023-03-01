package minesweeper.core;

/**
 * Mine tile.
 */
public class Mine extends Tile {

    @Override
    public String toString() {
            return new String((Character.toChars(0x1F3F4)));
    }
}
