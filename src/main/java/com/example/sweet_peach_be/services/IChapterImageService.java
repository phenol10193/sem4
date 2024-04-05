

package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.ChapterImage;
import java.util.List;

public interface IChapterImageService {
    List<ChapterImage> getChapterImagesByChapterId(Long chapterId);

    ChapterImage saveChapterImage(Long chapterId, ChapterImage chapterImage);

    ChapterImage updateChapterImage(Long chapterId, Long imageId, ChapterImage chapterImage);

    void deleteChapterImage(Long chapterId, Long imageId);
}
