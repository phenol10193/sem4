
package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.Comment;
import java.util.List;
import java.util.Optional;

public interface ICommentService {

    Comment saveComment( Comment comment);

    Comment updateComment(Long commentId, Comment commentDetails);

    Comment getCommentsById(Long Id);

    List<Comment> getAllComment();

    void deleteComment(Long commentId);
}
