
package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.dtos.ComicListItem;
import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.Comic;
import java.util.List;

public interface IComicService {
    List<Comic> getAllComics();

    Comic createComic(Comic comic);
    Comic updateComic(Long id, Comic comic);

    List<Chapter> getChaptersByComicId(Long id);

    void deleteComic(Long id);

    List<ComicListItem> getAllComicItems();

    List<ComicListItem> getNewestComicItems(int limit);
    List<ComicListItem> getHotComicItems(String period, int limit);
    List<ComicListItem> getComicItemsByGenreId(Long genreId);
    List<Comic> getNewestComics(int limit);

    List<Comic> getHotComics(String period, int limit);
    List<Comic> getComicsByGenreId(Long genreId);
    Comic getComicById(Long id);

    List<Comic> getReadComicsByUserId(Long userId);

    List<ComicListItem> getComicHistory(Long userId);

    void addGenreToComic(Long comicId, Long genreId);

    void removeGenreFromComic(Long comicId, Long genreId);
}
