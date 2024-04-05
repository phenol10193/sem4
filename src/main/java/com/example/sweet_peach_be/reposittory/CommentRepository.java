
package com.example.sweet_peach_be.reposittory;

import com.example.sweet_peach_be.models.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByChapterIdAndIsDeletedFalse(Long chapterId);

    List<Comment> findByUserIdAndIsDeletedFalse(Long userId);

    List<Comment> findByComicIdAndIsDeletedFalse(Long comicId);

    Optional<Comment> findByIdAndChapterIdAndComicId(Long id, Long chapterId, Long comicId);

    Optional<Comment> findByIdAndChapterIdAndUserIdAndComicId(Long id, Long chapterId, Long userId, Long comicId);
}
