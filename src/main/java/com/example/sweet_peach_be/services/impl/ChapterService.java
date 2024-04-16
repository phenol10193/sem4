
package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.ChapterImage;
import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.repositories.ComicRepository;
import com.example.sweet_peach_be.services.IChapterService;
import com.example.sweet_peach_be.services.UploadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.repositories.ChapterRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterService implements IChapterService {
    @Autowired
    private  ChapterRepository chapterRepository;
    @Autowired
    private  ComicRepository comicRepository;
    @Autowired
    private ChapterImageService chapterImageService;
    @Autowired
    private UploadService uploadService;
    private  ComicService comicService;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, ComicRepository comicRepository) {
        this.chapterRepository = chapterRepository;
        this.comicRepository = comicRepository;
    }

    public List<Chapter> getAllChapters() {
        return chapterRepository.findByIsDeletedFalse();
    }
    // Lấy thông tin Chapter theo comicId và chapterId
    public Optional<Chapter> getChapterByComicAndId(Long comicId, Long chapterId) {
        return chapterRepository.findByComicIdAndId(comicId, chapterId);
    }
    @Override
    public Chapter createChapter(Long comicId, Chapter chapter) {
        // Find the corresponding comic by comicId
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found with id: " + comicId));

        // Set the comic for the chapter
        chapter.setComic(comic);
        chapter.setUpdatedAt(LocalDateTime.now());

        return chapterRepository.save(chapter);
    }
    public Chapter updateChapter(Long id, Chapter updatedChapter) {
        Chapter existingChapter = chapterRepository.findById(id).orElse(null);
        if (existingChapter != null) {
            existingChapter.setTitle(updatedChapter.getTitle());
            existingChapter.setChapterNumber(updatedChapter.getChapterNumber());
            existingChapter.setUpdatedAt(LocalDateTime.now());
            // Cập nhật các trường khác nếu cần
            return chapterRepository.save(existingChapter);
        }
        return null;
    }
    @Transactional
    @Override
    public Chapter saveChapterWithImages(Chapter chapter, List<MultipartFile> chapterImages) throws IOException {
        // Lưu danh sách ChapterImage
        for (MultipartFile image : chapterImages) {
            String imagePath = uploadService.storeImage(image);
            ChapterImage chapterImage = new ChapterImage();
            chapterImage.setChapter(chapter);
            chapterImage.setImagePath(imagePath);
            chapter.getChapterImages().add(chapterImage);
        }

        // Lưu Chapter vào cơ sở dữ liệu
        return chapterRepository.save(chapter);
    }
    @Transactional
    @Override
    public Chapter updateChapterImages(Long id, List<MultipartFile> chapterImages) throws IOException {
        Optional<Chapter> optionalChapter = chapterRepository.findById(id);
        if (optionalChapter.isPresent()) {
            Chapter chapter = optionalChapter.get();

            // Lấy danh sách ChapterImages hiện tại của Chapter
            List<ChapterImage> existingChapterImages = chapter.getChapterImages();

            // Khởi tạo danh sách mới để lưu các ChapterImage mới

            // Lưu các ChapterImage hiện tại vào danh sách mới
            List<ChapterImage> newChapterImages = new ArrayList<>(existingChapterImages);

            // Lưu các ChapterImage mới từ các file ảnh được upload
            for (MultipartFile imageFile : chapterImages) {
                String imagePath = uploadService.storeImage(imageFile);
                ChapterImage newChapterImage = new ChapterImage();
                newChapterImage.setImagePath(imagePath);
                newChapterImage.setChapter(chapter);
                newChapterImages.add(newChapterImage);
            }

            // Cập nhật danh sách ChapterImages của Chapter và lưu lại vào cơ sở dữ liệu
            chapter.setChapterImages(newChapterImages);
            return chapterRepository.save(chapter);
        } else {
            throw new IllegalArgumentException("Chapter not found with id: " + id);
        }
    }
    public void deleteChapter(Long id) {
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        if (chapter != null) {
            chapter.setDeleted(true);
            chapterRepository.save(chapter);
        }
    }

    public Optional<Chapter> getChapterById(Long id) {
        return chapterRepository.findById(id);
    }
    public String getComicTitleByChapterId(Long chapterId) {
        Long comicId = chapterRepository.findComicIdByChapterId(chapterId);
        return comicService.getComicTitle(comicId);
    }
    @Override
    public List<Chapter> getChaptersByComicId(Long comicId) {
        return chapterRepository.findByComicIdAndIsDeletedFalse(comicId);
    }
    @Override
    public Chapter incrementViewCount(Long id) {
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        if (chapter != null) {
            int currentViewCount = chapter.getViewCount();
            chapter.setViewCount(currentViewCount + 1);
            chapter = chapterRepository.save(chapter);


            Comic comic = comicRepository.findById(chapter.getComicId()).orElse(null);
            if (comic != null) {
                comic.setViewCount(comic.getViewCount() + 1);
                comicRepository.save(comic);
            }

            return chapter;
        }
        return null;
    }

}
