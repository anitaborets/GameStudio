package service;

import entity.Score;
import exceptions.ScoreException;

import java.util.List;

public interface ScoreService {
    public void createScoreTable() throws ScoreException;
    public int insertScore(Score score) throws ScoreException;
    public List<Score> getBestScores(String gameName) throws ScoreException;
}
