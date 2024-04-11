
package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.Comment;

import java.util.List;

import com.example.sweet_peach_be.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/comments"})
@CrossOrigin
public class CommentController {
    private final ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping({"/users/{Id}"})
    public ResponseEntity<List<Comment>> getCommentsById(@PathVariable Long Id) {
        Comment comment = commentService.getCommentsById(Id);
        return new ResponseEntity(comment, comment != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/create"})
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        Comment savedComment = this.commentService.saveComment( comment);
        return new ResponseEntity(savedComment, HttpStatus.CREATED);
    }

    @PutMapping({"/update/{commentId}"})
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment commentDetails) {
        Comment updatedComment = this.commentService.updateComment(commentId, commentDetails);
        return new ResponseEntity(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping({"/{commentId}"})
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        this.commentService.deleteComment(commentId);
        return new ResponseEntity("Comment deleted successfully", HttpStatus.OK);
    }
}
