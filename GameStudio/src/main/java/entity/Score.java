package entity;

import java.sql.Timestamp;

public class Score {
    private int id;
    private String player;
    private String game;
    private int score;
    private Timestamp playedOn;


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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Timestamp getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Timestamp playedOn) {
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "Score{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", score=" + score +
                '}';
    }
}
