package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.ChapterImage;
import com.example.sweet_peach_be.services.IChapterImageService;
import java.util.List;

import com.example.sweet_peach_be.services.IChapterService;
import com.example.sweet_peach_be.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/api/chapter-images"})
public class ChapterImageController {
    private final IChapterImageService chapterImageService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private  IChapterService chapterService;
    @Autowired
    public ChapterImageController(IChapterImageService chapterImageService) {
        this.chapterImageService = chapterImageService;
    }

    @GetMapping({"/{chapterId}"})
    public List<ChapterImage> getChapterImagesByChapterId(@PathVariable Long chapterId) {
        return this.chapterImageService.getChapterImagesByChapterId(chapterId);
    }


    @PostMapping("/create")
    public ResponseEntity<ChapterImage> createChapterImage(@RequestParam("chapterId") Long chapterId,
                                                           @RequestParam("file") MultipartFile file) {
        try {
            // Lưu ảnh và nhận đường dẫn
            String imagePath = uploadService.storeImage(file);

            // Tạo mới đối tượng ChapterImage và lưu vào cơ sở dữ liệu
            ChapterImage chapterImage = new ChapterImage();
            Chapter
                    chapter = chapterService.getChapterById(chapterId);
            if (chapter == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            chapterImage.setChapter(chapter);
            chapterImage.setImagePath(imagePath);
            ChapterImage createdChapterImage = chapterImageService.createChapterImage(chapterImage);

            return new ResponseEntity<>(createdChapterImage, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{chapterImageId}")
    public ResponseEntity<ChapterImage> updateChapterImage(@PathVariable Long chapterImageId,
                                                           @RequestParam("chapterId") Long chapterId,
                                                           @RequestParam("file") MultipartFile file) {
        try {
            // Lưu ảnh và nhận đường dẫn
            String imagePath = uploadService.storeImage(file);

            // Lấy thông tin hình ảnh chương cũ từ cơ sở dữ liệu
            ChapterImage existingChapterImage = chapterImageService.getChapterImageById(chapterImageId);
            if (existingChapterImage == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Cập nhật thông tin hình ảnh chương và lưu vào cơ sở dữ liệu
            Chapter chapter = chapterService.getChapterById(chapterId);
            if (chapter == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            existingChapterImage.setChapter(chapter);
            existingChapterImage.setImagePath(imagePath);
            ChapterImage updatedChapterImage = chapterImageService.updateChapterImage(chapterImageId,existingChapterImage);

            return new ResponseEntity<>(updatedChapterImage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping({"/{imageId}"})
    public void deleteChapterImage(@PathVariable Long imageId) {
        this.chapterImageService.deleteChapterImage(imageId);
    }
}