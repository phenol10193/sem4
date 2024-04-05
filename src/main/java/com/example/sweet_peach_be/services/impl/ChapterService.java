

package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.exceptions.ResourceNotFoundException;
import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.reposittory.ChapterRepository;
import com.example.sweet_peach_be.reposittory.ComicRepository;
import com.example.sweet_peach_be.services.IChapterService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChapterService implements IChapterService {
    private final ChapterRepository chapterRepository;
    private final ComicRepository comicRepository;

    public ChapterService(ChapterRepository chapterRepository, ComicRepository comicRepository) {
        this.chapterRepository = chapterRepository;
        this.comicRepository = comicRepository;
    }

    public List<Chapter> getChaptersByComicId(Long comicId) {
        Optional<Comic> optionalComic = this.comicRepository.findById(comicId);
        if (optionalComic.isEmpty()) {
            throw new IllegalArgumentException("Comic not found with ID: " + comicId);
        } else {
            return this.chapterRepository.findByComicIdAndIsDeletedFalse(comicId);
        }
    }

    public Chapter createChapter(Long comicId, Chapter chapter) {
        Optional<Comic> optionalComic = this.comicRepository.findById(comicId);
        if (optionalComic.isEmpty()) {
            throw new IllegalArgumentException("Comic not found with ID: " + comicId);
        } else {
            Comic comic = (Comic)optionalComic.get();
            chapter.setComic(comic);
            return (Chapter)this.chapterRepository.save(chapter);
        }
    }

    public Chapter updateChapter(Long chapterId, Long comicId, Chapter chapterDetails) {
        Chapter chapter = (Chapter)this.chapterRepository.findByIdAndComicId(chapterId, comicId).orElseThrow(() -> {
            return new ResourceNotFoundException("Chapter not found with ID: " + chapterId + " and Comic ID: " + comicId);
        });
        chapter.setTitle(chapterDetails.getTitle());
        chapter.setChapterNumber(chapterDetails.getChapterNumber());
        chapter.setViewCount(chapterDetails.getViewCount());
        return (Chapter)this.chapterRepository.save(chapter);
    }

    public void deleteChapter(Long chapterId, Long comicId) {
        Optional<Chapter> optionalChapter = this.chapterRepository.findByIdAndComicId(chapterId, comicId);
        if (optionalChapter.isPresent()) {
            Chapter chapter = (Chapter)optionalChapter.get();
            chapter.setDeleted(true);
            this.chapterRepository.save(chapter);
        } else {
            throw new IllegalArgumentException("Chapter not found with ID: " + chapterId + " and Comic ID: " + comicId);
        }
    }
}
