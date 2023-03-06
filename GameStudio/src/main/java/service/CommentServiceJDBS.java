package service;

import com.zaxxer.hikari.HikariDataSource;
import entity.Comment;
import exceptions.CommentException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static entity.Constants.*;

public class CommentServiceJDBS implements CommentService {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS comments (id INT PRIMARY KEY Generated Always as Identity, player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, comment VARCHAR(200), commentedOn TIMESTAMP)";
    private static final String INSERT = "INSERT INTO comments (player,game,comment,commentedOn) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL = "SELECT * FROM comments LIMIT 100";
    private static final String DELETE = "TRUNCATE comments";
    HikariDataSource ds = HikariCPDataSource.getHikariDataSource();
    Logger LOGGER = Logger.getLogger(CommentServiceJDBS.class.getName());
    Connection con = null;
    PreparedStatement pst = null;

    public void createCommentTable() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
           // con = ds.getConnection();

            Statement stmt = con.createStatement();
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int addComment(Comment comment) throws CommentException {
        int count = 0;

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
            //con = ds.getConnection();
            pst = con.prepareStatement(INSERT);
            pst.setString(1, comment.getPlayer());
            pst.setString(2, comment.getGame());
            pst.setString(3, comment.getComment());
            pst.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            count = pst.executeUpdate();
            System.out.println(count);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        return count;
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> comments = new ArrayList<>();

        try {
            con = ds.getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(GET_ALL);

            while (results.next()) {
                Comment comment = new Comment();
                comment.setId(results.getInt(1));
                comment.setPlayer(results.getString(2));
                comment.setGame(results.getString(3));
                comment.setComment(results.getString(4));
                comment.setCommentedOn(results.getTimestamp(5));
                comments.add(comment);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        return comments;
    }

    @Override
    public void reset() throws CommentException {
        try {
            con = ds.getConnection();
            Statement s = con.createStatement();
            s.executeUpdate(DELETE);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new CommentException(e.getMessage());
        }
    }

}
