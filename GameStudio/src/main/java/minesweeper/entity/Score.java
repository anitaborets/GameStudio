package minesweeper.entity;

import java.sql.Timestamp;

public class Score {
    private int id;
    private String player;
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

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", player='" + player + '\'' +
                ", score=" + score +
                ", playedOn=" + playedOn +
                '}';
    }
}
