

package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.ChapterImage;
import com.example.sweet_peach_be.reposittory.ChapterImageRepository;
import com.example.sweet_peach_be.reposittory.ChapterRepository;
import com.example.sweet_peach_be.services.IChapterImageService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChapterImageService implements IChapterImageService {
    private final ChapterImageRepository chapterImageRepository;
    private final ChapterRepository chapterRepository;

    public ChapterImageService(ChapterRepository chapterRepository, ChapterImageRepository chapterImageRepository) {
        this.chapterRepository = chapterRepository;
        this.chapterImageRepository = chapterImageRepository;
    }

    public List<ChapterImage> getChapterImagesByChapterId(Long chapterId) {
        Optional<Chapter> optionalChapter = this.chapterRepository.findById(chapterId);
        if (optionalChapter.isEmpty()) {
            throw new IllegalArgumentException("Chapter not found with ID: " + chapterId);
        } else {
            return this.chapterImageRepository.findByChapterIdAndIsDeletedFalse(chapterId);
        }
    }

    public ChapterImage saveChapterImage(Long chapterId, ChapterImage chapterImage) {
        Optional<Chapter> optionalChapter = this.chapterRepository.findById(chapterId);
        if (optionalChapter.isPresent()) {
            Chapter chapter = (Chapter)optionalChapter.get();
            chapterImage.setChapter(chapter);
            return (ChapterImage)this.chapterImageRepository.save(chapterImage);
        } else {
            throw new IllegalArgumentException("Chapter not found with ID: " + chapterId);
        }
    }

    public ChapterImage updateChapterImage(Long chapterId, Long imageId, ChapterImage chapterImage) {
        Optional<ChapterImage> optionalChapterImage = this.chapterImageRepository.findByIdAndChapterId(imageId, chapterId);
        if (optionalChapterImage.isPresent()) {
            ChapterImage existingChapterImage = (ChapterImage)optionalChapterImage.get();
            return (ChapterImage)this.chapterImageRepository.save(existingChapterImage);
        } else {
            throw new IllegalArgumentException("Chapter Image not found with ID: " + imageId + " for Chapter ID: " + chapterId);
        }
    }

    public void deleteChapterImage(Long chapterId, Long imageId) {
        Optional<ChapterImage> optionalChapterImage = this.chapterImageRepository.findByIdAndChapterId(chapterId, imageId);
        if (optionalChapterImage.isPresent()) {
            ChapterImage chapterImage = (ChapterImage)optionalChapterImage.get();
            chapterImage.setDeleted(true);
            this.chapterImageRepository.save(chapterImage);
        } else {
            throw new IllegalArgumentException("Chapter not found with ID: " + chapterId + " and Image ID: " + imageId);
        }
    }
}
