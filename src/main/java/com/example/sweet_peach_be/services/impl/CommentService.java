

package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.models.Comment;
import com.example.sweet_peach_be.models.User;
import com.example.sweet_peach_be.reposittory.ChapterRepository;
import com.example.sweet_peach_be.reposittory.ComicRepository;
import com.example.sweet_peach_be.reposittory.CommentRepository;
import com.example.sweet_peach_be.reposittory.UserRepository;
import com.example.sweet_peach_be.services.ICommentService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final ChapterRepository chapterRepository;
    private final ComicRepository comicRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ChapterRepository chapterRepository, ComicRepository comicRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.chapterRepository = chapterRepository;
        this.comicRepository = comicRepository;
        this.userRepository = userRepository;
    }

    public List<Comment> getCommentsByUser(Long userId) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        } else {
            return this.commentRepository.findByUserIdAndIsDeletedFalse(userId);
        }
    }

    public List<Comment> getCommentsByComic(Long comicId) {
        Optional<Comic> optionalComic = this.comicRepository.findById(comicId);
        if (!optionalComic.isPresent()) {
            throw new IllegalArgumentException("Comic not found with ID: " + comicId);
        } else {
            return this.commentRepository.findByComicIdAndIsDeletedFalse(comicId);
        }
    }

    public List<Comment> getCommentsByChapter(Long chapterId) {
        Optional<Chapter> optionalChapter = this.chapterRepository.findById(chapterId);
        if (!optionalChapter.isPresent()) {
            throw new IllegalArgumentException("Chapter not found with ID: " + chapterId);
        } else {
            return this.commentRepository.findByChapterIdAndIsDeletedFalse(chapterId);
        }
    }

    public void deleteComment(Long commentId, Long chapterId, Long userId, Long comicId) {
        Optional<Comment> optionalComment = this.commentRepository.findByIdAndChapterIdAndUserIdAndComicId(commentId, chapterId, userId, comicId);
        if (optionalComment.isPresent()) {
            Comment comment = (Comment)optionalComment.get();
            comment.setDeleted(true);
            this.commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("Comment not found with ID: " + commentId + ", Chapter ID: " + chapterId + ", Comic ID: " + comicId + " and User ID: " + userId);
        }
    }

    public Comment saveComment(Long comicId, Long chapterId, Long userId, Comment comment) {
        Optional<Comic> optionalComic = this.comicRepository.findById(comicId);
        if (!optionalComic.isPresent()) {
            throw new IllegalArgumentException("Comic not found with ID: " + comicId);
        } else {
            Comic comic = (Comic)optionalComic.get();
            Optional<Chapter> optionalChapter = this.chapterRepository.findById(chapterId);
            if (!optionalChapter.isPresent()) {
                throw new IllegalArgumentException("Chapter not found with ID: " + chapterId);
            } else {
                Chapter chapter = (Chapter)optionalChapter.get();
                Optional<User> optionalUser = this.userRepository.findById(userId);
                if (!optionalUser.isPresent()) {
                    throw new IllegalArgumentException("User not found with ID: " + userId);
                } else {
                    User user = (User)optionalUser.get();
                    comment.setComic(comic);
                    comment.setChapter(chapter);
                    comment.setUser(user);
                    comment.setCreatedAt(new Date());
                    return (Comment)this.commentRepository.save(comment);
                }
            }
        }
    }

    public Comment updateComment(Long commentId, Long comicId, Long chapterId, Long userId, Comment commentDetails) {
        Comment comment = (Comment)this.commentRepository.findById(commentId).orElse((Object)null);
        if (comment == null) {
            throw new IllegalArgumentException("Comment not found with ID: " + commentId);
        } else {
            Optional<Comic> optionalComic = this.comicRepository.findById(comicId);
            if (!optionalComic.isPresent()) {
                throw new IllegalArgumentException("Comic not found with ID: " + comicId);
            } else {
                Optional<Chapter> optionalChapter = this.chapterRepository.findById(chapterId);
                if (!optionalChapter.isPresent()) {
                    throw new IllegalArgumentException("Chapter not found with ID: " + chapterId);
                } else {
                    Optional<User> optionalUser = this.userRepository.findById(userId);
                    if (!optionalUser.isPresent()) {
                        throw new IllegalArgumentException("User not found with ID: " + userId);
                    } else {
                        comment.setContent(commentDetails.getContent());
                        comment.setCreatedAt(new Date());
                        return (Comment)this.commentRepository.save(comment);
                    }
                }
            }
        }
    }
}
