
package com.example.sweet_peach_be.repositories;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.ChapterImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterImageRepository extends JpaRepository<ChapterImage, Long> {
    Optional<Chapter> findByChapterId(Long chapterId);

    List<ChapterImage> findByChapterIdAndIsDeletedFalse(Long chapterId);

    Optional<ChapterImage> findByIdAndChapterId(Long imageId, Long chapterId);

    List<ChapterImage> findChapterImagesByChapterId(Long chapterId);
}
