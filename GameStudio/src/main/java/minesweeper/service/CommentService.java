package minesweeper.service;

import minesweeper.entity.Comment;
import minesweeper.exceptions.CommentException;

import java.util.List;

public interface CommentService {
    int addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    void reset() throws CommentException;
}
