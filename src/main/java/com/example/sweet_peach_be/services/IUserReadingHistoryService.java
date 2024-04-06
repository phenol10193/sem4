package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.User;
import com.example.sweet_peach_be.models.UserReadingHistory;
import com.example.sweet_peach_be.models.Chapter;

import java.util.List;

public interface IUserReadingHistoryService {
    List<UserReadingHistory> getUserReadingHistoryByUser(User user);
    List<Chapter> getChaptersReadByUserAndComic(User user, Long comicId);
    UserReadingHistory getUserReadingHistoryById(Long historyId);
    void addUserReadingHistory(User user, Long comicId, Long chapterId);
    void updateReadStatus(Long historyId, boolean isRead);
    void deleteUserReadingHistory(User user, Long comicId);
}
