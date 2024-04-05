package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.services.IChapterService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/chapters"})
public class ChapterController {
    private final IChapterService chapterService;

    public ChapterController(IChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping({"/comic/{comicId}"})
    public ResponseEntity<List<Chapter>> getChaptersByComicId(@PathVariable Long comicId) {
        List<Chapter> chapters = this.chapterService.getChaptersByComicId(comicId);
        return ResponseEntity.ok(chapters);
    }

    @PostMapping({"/comic/{comicId}"})
    public ResponseEntity<Chapter> createChapter(@PathVariable Long comicId, @RequestBody Chapter chapter) {
        Chapter createdChapter = this.chapterService.createChapter(comicId, chapter);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChapter);
    }

    @PutMapping({"/{chapterId}/comic/{comicId}"})
    public ResponseEntity<Chapter> updateChapter(@PathVariable Long chapterId, @PathVariable Long comicId, @RequestBody Chapter chapterDetails) {
        Chapter updatedChapter = this.chapterService.updateChapter(chapterId, comicId, chapterDetails);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping({"/{chapterId}/comic/{comicId}"})
    public ResponseEntity<Void> deleteChapter(@PathVariable Long chapterId, @PathVariable Long comicId) {
        this.chapterService.deleteChapter(chapterId, comicId);
        return ResponseEntity.noContent().build();
    }
}