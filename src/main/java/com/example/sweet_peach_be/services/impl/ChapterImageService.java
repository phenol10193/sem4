

package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.ChapterImage;
import com.example.sweet_peach_be.repositories.ChapterImageRepository;
import com.example.sweet_peach_be.repositories.ChapterRepository;
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
    @Override
    public ChapterImage getChapterImageById( Long chapterImageId){
        return chapterImageRepository.findById(chapterImageId).orElse(null);
    }

    public List<ChapterImage> getChapterImagesByChapterId(Long chapterId) {
        Optional<Chapter> optionalChapter = this.chapterRepository.findById(chapterId);
        if (optionalChapter.isEmpty()) {
            throw new IllegalArgumentException("Chapter not found with ID: " + chapterId);
        } else {
            return this.chapterImageRepository.findByChapterIdAndIsDeletedFalse(chapterId);
        }
    }

    public ChapterImage createChapterImage(ChapterImage chapterImage) {
        return chapterImageRepository.save(chapterImage);
    }

    public ChapterImage updateChapterImage(Long imageId, ChapterImage chapterImage) {
        chapterImage.setId(imageId);
        return chapterImageRepository.save(chapterImage);
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
