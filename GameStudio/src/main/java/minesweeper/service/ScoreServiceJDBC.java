package minesweeper.service;

import com.zaxxer.hikari.HikariDataSource;
import minesweeper.entity.Score;
import minesweeper.exceptions.ScoreException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static minesweeper.Constants.*;
import static minesweeper.service.CommentServiceJDBS.*;
import static minesweeper.service.HikariCPDataSource.getHikariDataSource;

public class ScoreServiceJDBC implements ScoreService {
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS score (id INT PRIMARY KEY Generated Always as Identity, player VARCHAR(32) NOT NULL, score INT NOT NULL, playedOn TIMESTAMP)";
    private static final String GET_ALL = "SELECT * FROM score LIMIT 10";
    private static final String DELETE = "TRUNCATE score";
    private static final String INSERT = "INSERT INTO score (player,score,playedOn) VALUES (?, ?, ?)";
    Connection con = null;
    PreparedStatement pst = null;
    HikariDataSource ds = getHikariDataSource();
    Logger LOGGER = Logger.getLogger(ScoreServiceJDBC.class.getName());

    public void createScoreTable() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(CREATE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }

    public int insertScore(Score score) {
        int count = 0;

        try {
            con = ds.getConnection();
            pst = con.prepareStatement(INSERT);
            pst.setString(1, score.getPlayer());
            pst.setInt(2, score.getScore());
            pst.setTimestamp(3, new Timestamp(score.getPlayedOn().getTime()));

            count = pst.executeUpdate();
            System.out.println(count);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return count;
    }

    public List<Score> getBestScores() {
        List<Score> scores = new ArrayList<>();
        try {
            con = ds.getConnection();
            pst = con.prepareStatement(GET_ALL);
            ResultSet results = pst.executeQuery();
            while (results.next()) {
                Score score = new Score();
                score.setId(results.getInt(1));
                score.setPlayer(results.getString(2));
                score.setScore(results.getInt(3));
                score.setPlayedOn(results.getTimestamp(4));
                scores.add(score);
                System.out.println(score);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public void reset() throws ScoreException {
        try {
            con = ds.getConnection();
            Statement statement = con.createStatement();
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ScoreException(e.getMessage(), e.getMessage());
        }
    }
}
