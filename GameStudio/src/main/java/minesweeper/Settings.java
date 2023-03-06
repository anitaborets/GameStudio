package minesweeper;

import java.io.*;
import java.util.Objects;

public class Settings implements Serializable {
    static final long serialVersionUID = 1L;
    private final int rowCount;
    private final int columnCount;
    private final int mineCount;
    public static final Settings BEGINNER = new Settings(9, 9, 10);
    public static final Settings INTERMEDIATE = new Settings(16, 16, 40);
    public static final Settings EXPERT = new Settings(16, 30, 99);
    private static final String SETTING_FILE = System.getProperty("user.home") + System.getProperty("file.separator") + "minesweeper.settings";

    public Settings(int rowCount, int columnCount, int mineCount) {

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Settings settings)) return false;
        return getRowCount() == settings.getRowCount() && getColumnCount() == settings.getColumnCount() && getMineCount() == settings.getMineCount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRowCount()* getColumnCount()* getMineCount());
    }

    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(SETTING_FILE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);

        fileOutputStream.close();
        objectOutputStream.close();
    }

    public static Settings load() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(SETTING_FILE);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Settings settings = (Settings) objectInputStream.readObject();

        objectInputStream.close();

        return settings;
    }
}
