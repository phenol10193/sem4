

package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.ChapterImage;
import java.util.List;

public interface IChapterImageService {
    List<ChapterImage> getChapterImagesByChapterId(Long chapterId);
   ChapterImage getChapterImageById( Long chapterImageId);
    ChapterImage createChapterImage(ChapterImage chapterImage);

    ChapterImage updateChapterImage(Long imageId, ChapterImage chapterImage);

    void deleteChapterImage(Long imageId);


}
