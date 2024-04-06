

package com.example.sweet_peach_be.services.impl;


import com.example.sweet_peach_be.models.Comment;
import com.example.sweet_peach_be.repositories.CommentRepository;
import com.example.sweet_peach_be.services.ICommentService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;

    }
    @Override
    public List<Comment> getAllComment() {
        return commentRepository.findAllByIsDeletedFalse();
    }
    @Override
    public Comment getCommentsById(Long Id) {
        return commentRepository.findById(Id).orElse(null);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setDeleted(true);
            commentRepository.save(comment);
        }
    }
    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
    @Override
    public Comment updateComment(Long commentId, Comment commentDetails) {
        commentDetails.setId(commentId);
        return commentRepository.save(commentDetails);
    }
}
