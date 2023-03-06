package entity;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private String player;
    private String game;
    private String comment;
    private Timestamp commentedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Timestamp commentedOn) {
        this.commentedOn = commentedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", comment='" + comment + '\'' +
                ", commentedOn=" + commentedOn +
                '}';
    }
}
