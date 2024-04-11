package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.services.IChapterService;
import com.example.sweet_peach_be.services.impl.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sweet_peach_be.models.Chapter;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/chapters")
public class ChapterController {

    private final IChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping
    public ResponseEntity<List<Chapter>> getAllChapters() {
        List<Chapter> chapters = chapterService.getAllChapters();
        return new ResponseEntity<>(chapters, HttpStatus.OK);
    }
    @GetMapping("/comic/{comicId}")
    public ResponseEntity<List<Chapter>> getChaptersByComicId(@PathVariable Long comicId) {
        List<Chapter> chapters = chapterService.getChaptersByComicId(comicId);
        return new ResponseEntity<>(chapters, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<Chapter> createChapter(@RequestBody Chapter chapter) {
        Chapter createdChapter = chapterService.createChapter(chapter);
        return new ResponseEntity<>(createdChapter, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Chapter> updateChapter(@PathVariable("id") Long id, @RequestBody Chapter updatedChapter) {
        Chapter chapter = chapterService.updateChapter(id, updatedChapter);
        if (chapter != null) {
            return new ResponseEntity<>(chapter, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable("id") Long id) {
        chapterService.deleteChapter(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Chapter> incrementViewCount(@PathVariable("id") Long id) {
        Chapter chapter = chapterService.incrementViewCount(id);
        if (chapter != null) {
            return new ResponseEntity<>(chapter, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}