package service;

import com.zaxxer.hikari.HikariDataSource;
import entity.Rating;
import exceptions.RatingException;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static entity.Constants.*;

public class RatingServiceJDBC implements RatingService {
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS rating (id INT PRIMARY KEY Generated Always as Identity, player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, rating INT NOT NULL, ratedOn TIMESTAMP)";
    private static final String DELETE = "TRUNCATE rating";
    private static final String INSERT = "INSERT INTO rating (player,game,rating,ratedOn) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE rating SET rating=? WHERE player=? AND game=?";
    private static final String GET_PLAYER = "SELECT * FROM rating WHERE player=? AND game=?";
    private static final String GET_GAME = "SELECT rating FROM rating WHERE game=?";
    private static final String GET_AVG = "SELECT AVG(rating) FROM rating WHERE game=?";

    Connection con = null;
    PreparedStatement pst = null;
    HikariDataSource ds = HikariCPDataSource.getHikariDataSource();
    Logger LOGGER = Logger.getLogger(RatingServiceJDBC.class.getName());

    public void createRatingTable() {
        //todo with property file
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
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

        if (getRating(rating.getGame(), rating.getPlayer()) == -1) {
            try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
                pst = con.prepareStatement(INSERT);
                pst.setString(1, rating.getPlayer());
                pst.setString(2, rating.getGame());
                pst.setInt(3, rating.getRating());
                pst.setTimestamp(4, rating.getRatedOn());

                count = pst.executeUpdate();
                LOGGER.log(Level.INFO, "rating was added " + count);

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        } else {
            try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

                pst = con.prepareStatement(UPDATE);
                pst.setInt(1, rating.getRating());
                pst.setString(2, rating.getPlayer());
                pst.setString(3, rating.getGame());

                count = pst.executeUpdate();
                LOGGER.log(Level.INFO, "rating was update " + count);

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }

        return count;
    }

    @Override
    public double getAverageRating(String game) throws RatingException {

        double rating = -1;
        try {
            con = ds.getConnection();
            pst = con.prepareStatement(GET_GAME);
            pst.setString(1, game);
            ResultSet results = pst.executeQuery();

            if (results.next()) {
                LOGGER.log(Level.INFO, "Game has rating");

                //pst = con.prepareStatement(GET_AVG);
                 //pst.setString(1, game);
                pst = con.prepareStatement(GET_AVG);
                pst.setString(1,game);
               results = pst.executeQuery();

                while (results.next()) {

                     rating = results.getDouble(1);
                }

                }
            } catch(SQLException e){
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }

        //averageRating = ratings.stream().mapToInt(Integer::intValue).summaryStatistics().getAverage();
        return rating;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating = -1;
        try {
            con = ds.getConnection();
            pst = con.prepareStatement(GET_PLAYER);
            pst.setString(1, player);
            pst.setString(2, game);
            ResultSet results = pst.executeQuery();

            if (results.next()) {
                LOGGER.log(Level.INFO, "Rating already exists");
                rating = results.getInt(4);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return rating;
    }

    @Override
    public void reset() throws RatingException {
        try {
            con = ds.getConnection();
            Statement statement = con.createStatement();
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new RatingException(e.getMessage());
        }
    }


}
