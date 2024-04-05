package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.services.IChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.repositories.ChapterRepository;
import java.util.List;

@Service
public class ChapterService implements IChapterService {

    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public List<Chapter> getAllChapters() {
        return chapterRepository.findByIsDeletedFalse();
    }

    public Chapter createChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }
    @Override
    public Chapter getChapterById(Long id) {
        return chapterRepository.findById(id).orElse(null);
    }

    public Chapter updateChapter(Long id, Chapter updatedChapter) {
        updatedChapter.setId(id);
        return chapterRepository.save(updatedChapter);
    }

    public void deleteChapter(Long id) {
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        if (chapter != null) {
            chapter.setDeleted(true);
            chapterRepository.save(chapter);
        }
    }

    @Override
    public List<Chapter> getChaptersByComicId(Long comicId) {
        return chapterRepository.findByComicIdAndIsDeletedFalse(comicId);
    }

    public Chapter incrementViewCount(Long id) {
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        if (chapter != null) {
            int currentViewCount = chapter.getViewCount();
            chapter.setViewCount(currentViewCount + 1);
            return chapterRepository.save(chapter);
        }
        return null;
    }
}

