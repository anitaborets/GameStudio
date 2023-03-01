package minesweeper.service;

import com.zaxxer.hikari.HikariDataSource;
import minesweeper.entity.Rating;
import minesweeper.exceptions.RatingException;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static minesweeper.Constants.*;
import static minesweeper.service.HikariCPDataSource.getHikariDataSource;

public class RatingServiceJDBC implements RatingService {
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS rating (id INT PRIMARY KEY Generated Always as Identity, player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, rating INT NOT NULL, date TIMESTAMP)";
    private static final String GET_ALL = "SELECT * FROM rating LIMIT 10";
    private static final String DELETE = "TRUNCATE rating";
    private static final String INSERT = "INSERT INTO rating (player,game,rating,date) VALUES (?, ?, ?, ?)";
    Connection con = null;
    PreparedStatement pst = null;
    HikariDataSource ds = getHikariDataSource();
    Logger LOGGER = Logger.getLogger(RatingServiceJDBC.class.getName());

    public void createRatingTable() {
        //todo with property file
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);) {
            //con = ds.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(CREATE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }
    @Override
    public int setRating(Rating rating) throws RatingException {
        int count = 0;

        try {
            con = ds.getConnection();
            pst = con.prepareStatement(INSERT);
            pst.setString(1, rating.getPlayer());
            pst.setString(2, rating.getGame());
            pst.setInt(3, rating.getRating());
            pst.setTimestamp(4, rating.getDate());

            count = pst.executeUpdate();
            LOGGER.log(Level.INFO, "rating was added " + count);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return count;

    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return 0;
    }

    @Override
    public void reset() throws RatingException {

    }
}
