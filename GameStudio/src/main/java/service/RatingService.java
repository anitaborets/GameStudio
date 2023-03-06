package service;

import entity.Rating;
import exceptions.RatingException;

public interface RatingService {
    int setRating(Rating rating) throws RatingException;

    double getAverageRating(String game) throws RatingException;

    int getRating(String game, String player) throws RatingException;

    void reset() throws RatingException;
}
