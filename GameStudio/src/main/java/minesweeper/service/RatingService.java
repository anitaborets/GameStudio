package minesweeper.service;

import minesweeper.entity.Rating;
import minesweeper.exceptions.RatingException;

public interface RatingService {
    int setRating(Rating rating) throws RatingException;

    int getAverageRating(String game) throws RatingException;

    int getRating(String game, String player) throws RatingException;

    void reset() throws RatingException;
}
