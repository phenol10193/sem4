
package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.Comment;
import java.util.List;

public interface ICommentService {
    Comment saveComment(Long comicId, Long chapterId, Long userId, Comment comment);

    Comment updateComment(Long commentId, Long comicId, Long chapterId, Long userId, Comment commentDetails);

    List<Comment> getCommentsByUser(Long userId);

    List<Comment> getCommentsByComic(Long comicId);

    List<Comment> getCommentsByChapter(Long chapterId);

    void deleteComment(Long commentId, Long chapterId, Long userId, Long comicId);
}
