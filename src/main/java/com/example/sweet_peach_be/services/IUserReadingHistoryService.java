package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.UserReadingHistory;

import java.util.List;

public interface IUserReadingHistoryService {
    void saveReadingHistory(Long userId, Long comicId, Long chapterId);
    List<UserReadingHistory> getReadingHistory(Long userId);
}
