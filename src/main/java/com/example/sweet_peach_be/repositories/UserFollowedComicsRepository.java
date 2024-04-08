package com.example.sweet_peach_be.repositories;

import com.example.sweet_peach_be.models.UserFollowedComics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowedComicsRepository extends JpaRepository<UserFollowedComics, Long> {
    List<UserFollowedComics> findByUserIdAndIsDeletedFalse(Long userId);
    Optional<UserFollowedComics> findByUserIdAndComicId(Long userId, Long comicId);
}
