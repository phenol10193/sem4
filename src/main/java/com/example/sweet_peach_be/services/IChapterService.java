

package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.Chapter;
import java.util.List;

public interface IChapterService {
    List<Chapter> getChaptersByComicId(Long comicId);

    Chapter createChapter(Long comicId, Chapter chapter);

    Chapter updateChapter(Long chapterId, Long comicId, Chapter chapterDetails);

    void deleteChapter(Long chapterId, Long comicId);
}
