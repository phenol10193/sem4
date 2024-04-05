
package com.example.sweet_peach_be.reposittory;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.Comic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByComicIdAndIsDeletedFalse(Long comicId);

    Optional<Comic> findComicById(Long comicId);

    Optional<Chapter> findByIdAndComicId(Long id, Long comicId);

    List<Chapter> findChaptersByComicId(Long comicId);
}
