package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.ChapterImage;
import com.example.sweet_peach_be.services.IChapterImageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/chapter-images"})
public class ChapterImageController {
    private final IChapterImageService chapterImageService;

    @Autowired
    public ChapterImageController(IChapterImageService chapterImageService) {
        this.chapterImageService = chapterImageService;
    }

    @GetMapping({"/{chapterId}"})
    public List<ChapterImage> getChapterImagesByChapterId(@PathVariable Long chapterId) {
        return this.chapterImageService.getChapterImagesByChapterId(chapterId);
    }

    @PostMapping({"/{chapterId}"})
    public ChapterImage saveChapterImage(@PathVariable Long chapterId, @RequestBody ChapterImage chapterImage) {
        return this.chapterImageService.saveChapterImage(chapterId, chapterImage);
    }

    @PutMapping({"/{chapterId}/{imageId}"})
    public ChapterImage updateChapterImage(@PathVariable Long chapterId, @PathVariable Long imageId, @RequestBody ChapterImage chapterImage) {
        return this.chapterImageService.updateChapterImage(chapterId, imageId, chapterImage);
    }

    @DeleteMapping({"/{chapterId}/{imageId}"})
    public void deleteChapterImage(@PathVariable Long chapterId, @PathVariable Long imageId) {
        this.chapterImageService.deleteChapterImage(chapterId, imageId);
    }
}