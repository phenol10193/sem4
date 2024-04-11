package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.services.IChapterService;
import com.example.sweet_peach_be.services.IComicService;
import com.example.sweet_peach_be.services.impl.ChapterService;
import com.example.sweet_peach_be.services.impl.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sweet_peach_be.models.Chapter;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@CrossOrigin
public class ChapterController {

    private final IChapterService chapterService;
    private final IComicService comicService;
    @Autowired
    public ChapterController(ChapterService chapterService, ComicService comicService) {
        this.chapterService = chapterService;
        this.comicService = comicService;

    }

    @GetMapping("")
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
    public ResponseEntity<Chapter> createChapter(@RequestParam("comicId") Long comicId,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("chapterNumber") int chapterNumber,
                                                 @RequestParam("viewCount") int viewCount) {
        try {
            // Your code here
            Chapter chapter = new Chapter();
            // Lấy đối tượng Comic từ comicId và gán cho chapter
            Comic comic = comicService.getComicById(comicId);
            if (comic == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            chapter.setComic(comic);
            chapter.setTitle(title);
            chapter.setChapterNumber(chapterNumber);
            chapter.setViewCount(viewCount);
            chapter.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            Chapter createdChapter = chapterService.createChapter(chapter);
            return new ResponseEntity<>(createdChapter, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Chapter> updateChapter(@PathVariable Long id,
                                                 @RequestParam("comicId") Long comicId,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("chapterNumber") int chapterNumber,
                                                 @RequestParam("viewCount") int viewCount) {
        try {
            Chapter existingChapter = chapterService.getChapterById(id);
            if (existingChapter == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Lấy đối tượng Comic từ comicId và gán cho chapter
            Comic comic = comicService.getComicById(comicId);
            if (comic == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            existingChapter.setComic(comic);
            existingChapter.setTitle(title);
            existingChapter.setChapterNumber(chapterNumber);
            existingChapter.setViewCount(viewCount);
            // Đặt thời gian hiện tại cho updatedAt
            existingChapter.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            Chapter updatedChapter = chapterService.updateChapter(id, existingChapter);
            return new ResponseEntity<>(updatedChapter, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
