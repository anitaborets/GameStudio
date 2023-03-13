package entity;

import exceptions.RatingException;

import java.sql.Timestamp;

public class Rating {
    private int id;
    private String player;
    private String game;
    private int rating;
    private Timestamp ratedOn;

    public Rating() {}

    public Rating(String player, String game, int rating, Timestamp ratedOn) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

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

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) throws RatingException {
        if(rating>=0 && rating<6) {
            this.rating = rating;
        }
        else {
            throw new RatingException("Rating may be from 0 to 5 only");
        }
    }

    public Timestamp getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Timestamp ratedOn) {
        this.ratedOn = ratedOn;
    }
}
