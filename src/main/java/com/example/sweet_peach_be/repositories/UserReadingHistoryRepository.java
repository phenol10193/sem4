package com.example.sweet_peach_be.repositories;

import com.example.sweet_peach_be.models.User;
import com.example.sweet_peach_be.models.UserReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReadingHistoryRepository extends JpaRepository<UserReadingHistory, Long> {
    List<UserReadingHistory> findByUser(User user);
    List<UserReadingHistory> findByUserAndComicId(User user, Long comicId);
    UserReadingHistory findByUserAndComicIdAndChapterId(User user, Long comicId, Long chapterId);
}
