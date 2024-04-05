package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.Chapter;

import java.util.List;

public interface IChapterService {
    public List<Chapter> getAllChapters();
    public Chapter createChapter(Chapter chapter);
    Chapter updateChapter(Long id, Chapter updatedChapter);
    public void deleteChapter(Long id);
    Chapter getChapterById(Long id);
    List<Chapter>getChaptersByComicId(Long comicId);
    Chapter incrementViewCount(Long id);
}
