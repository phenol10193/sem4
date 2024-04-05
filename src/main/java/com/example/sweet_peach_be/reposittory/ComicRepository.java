

package com.example.sweet_peach_be.reposittory;

import com.example.sweet_peach_be.models.Comic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    List<Comic> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String title);

    Optional<Comic> findByIdAndIsDeletedFalse(Long comicId);

    List<Comic> findByIsDeletedFalse();
}
