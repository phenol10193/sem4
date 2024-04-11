package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.UserReadingHistory;
import com.example.sweet_peach_be.repositories.ComicRepository;
import com.example.sweet_peach_be.repositories.UserReadingHistoryRepository;
import com.example.sweet_peach_be.services.IUserReadingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserReadingHistoryService implements IUserReadingHistoryService{

    @Autowired
    private UserReadingHistoryRepository historyRepository;

    @Override
    public void saveReadingHistory(Long userId, Long comicId, Long chapterId) {
        Optional<UserReadingHistory> existingHistory = historyRepository.findByUserIdAndComicIdAndChapterId(userId, comicId, chapterId);
        if (existingHistory.isPresent()) {
            // Nếu đã tồn tại thì không cần lưu lại
            return;
        }

        UserReadingHistory history = new UserReadingHistory();
        history.setUserId(userId);
        history.setComicId(comicId);
        history.setChapterId(chapterId);
        history.setDeleted(false);
        historyRepository.save(history);
    }
    @Override
    public List<UserReadingHistory> getReadingHistory(Long userId) {
        return historyRepository.findByUserId(userId);
    }

}