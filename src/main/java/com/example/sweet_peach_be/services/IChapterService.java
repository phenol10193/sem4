package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.Chapter;

import java.util.List;

public interface IChapterService {
    List<Chapter> getAllChapters();
    Chapter createChapter(Chapter chapter);
    Chapter updateChapter(Long id, Chapter updatedChapter);
    void deleteChapter(Long id);
    List<Chapter>getChaptersByComicId(Long comicId);
    Chapter incrementViewCount(Long id);
}