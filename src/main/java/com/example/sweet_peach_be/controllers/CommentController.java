
package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.Comment;

import java.util.List;

import com.example.sweet_peach_be.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/comments"})
public class CommentController {
    private final ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping({"/users/{userId}"})
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable Long userId) {
        List<Comment> comments = this.commentService.getCommentsByUser(userId);
        return new ResponseEntity(comments, HttpStatus.OK);
    }

    @GetMapping({"/comics/{comicId}"})
    public ResponseEntity<List<Comment>> getCommentsByComic(@PathVariable Long comicId) {
        List<Comment> comments = this.commentService.getCommentsByComic(comicId);
        return new ResponseEntity(comments, HttpStatus.OK);
    }

    @GetMapping({"/chapters/{chapterId}"})
    public ResponseEntity<List<Comment>> getCommentsByChapter(@PathVariable Long chapterId) {
        List<Comment> comments = this.commentService.getCommentsByChapter(chapterId);
        return new ResponseEntity(comments, HttpStatus.OK);
    }

    @PostMapping({"/{comicId}/{chapterId}/{userId}"})
    public ResponseEntity<Comment> addComment(@PathVariable Long comicId, @PathVariable Long chapterId, @PathVariable Long userId, @RequestBody Comment comment) {
        Comment savedComment = this.commentService.saveComment(comicId, chapterId, userId, comment);
        return new ResponseEntity(savedComment, HttpStatus.CREATED);
    }

    @PutMapping({"/{commentId}/{comicId}/{chapterId}/{userId}"})
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @PathVariable Long comicId, @PathVariable Long chapterId, @PathVariable Long userId, @RequestBody Comment commentDetails) {
        Comment updatedComment = this.commentService.updateComment(commentId, comicId, chapterId, userId, commentDetails);
        return new ResponseEntity(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping({"/{commentId}/{chapterId}/{userId}/{comicId}"})
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @PathVariable Long chapterId, @PathVariable Long userId, @PathVariable Long comicId) {
        this.commentService.deleteComment(commentId, chapterId, userId, comicId);
        return new ResponseEntity("Comment deleted successfully", HttpStatus.OK);
    }
}
