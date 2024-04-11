package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.User;
import com.example.sweet_peach_be.models.UserReadingHistory;
import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.repositories.ChapterRepository;
import com.example.sweet_peach_be.repositories.ComicRepository;
import com.example.sweet_peach_be.repositories.UserReadingHistoryRepository;
import com.example.sweet_peach_be.services.IUserReadingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserReadingHistoryService implements IUserReadingHistoryService {
    @Autowired
    private UserReadingHistoryRepository userReadingHistoryRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ComicRepository comicRepository;
    @Override
    public List<UserReadingHistory> getUserReadingHistoryByUser(User user) {
        return userReadingHistoryRepository.findByUser(user);
    }

    @Override
    public List<Chapter> getChaptersReadByUserAndComic(User user, Long comicId) {
        List<UserReadingHistory> userReadingHistoryList = userReadingHistoryRepository.findByUserAndComicId(user, comicId);
        List<Chapter> chaptersReadByUserAndComic = new ArrayList<>();
        for (UserReadingHistory history : userReadingHistoryList) {
            chaptersReadByUserAndComic.add(history.getChapter());
        }
        return chaptersReadByUserAndComic;
    }

    @Override
    public void addUserReadingHistory(User user, Long comicId, Long chapterId) {
        UserReadingHistory existingHistory = userReadingHistoryRepository.findByUserAndComicIdAndChapterId(user, comicId, chapterId);
        if (existingHistory!= null) {
            return;
        }

        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new IllegalArgumentException("Comic not found with ID: " + comicId));
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with ID: " + chapterId));

        UserReadingHistory history = new UserReadingHistory();
        history.setUser(user);
        history.setComic(comic);
        history.setChapter(chapter);
        userReadingHistoryRepository.save(history);
    }

    public void deleteUserReadingHistory(User user, Long comicId) {
        List<UserReadingHistory> userReadingHistoryList = userReadingHistoryRepository.findByUserAndComicId(user, comicId);
        for (UserReadingHistory history : userReadingHistoryList) {
            history.setDeleted(true);
        }
        userReadingHistoryRepository.saveAll(userReadingHistoryList);
    }

    @Override
    public UserReadingHistory getUserReadingHistoryById(Long historyId) {
        return userReadingHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("User reading history not found"));
    }

    @Override
    public void updateReadStatus(Long historyId, boolean isRead) {
        UserReadingHistory history = getUserReadingHistoryById(historyId);
        history.setIsRead(isRead);
        userReadingHistoryRepository.save(history);
    }
}
