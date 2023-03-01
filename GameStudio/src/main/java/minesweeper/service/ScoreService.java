package minesweeper.service;

import minesweeper.entity.Score;

import java.util.List;

public interface ScoreService {
    public void createScoreTable();
    public int insertScore(Score score);
    public List<Score> getBestScores();
}
