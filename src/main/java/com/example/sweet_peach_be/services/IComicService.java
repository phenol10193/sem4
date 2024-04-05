
package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.Comic;
import java.util.List;
import java.util.Optional;

public interface IComicService {
    List<Comic> getAllComics();

    Optional<Comic> getComicById(Long comicId);

    Comic createComic(Comic comic);

    Comic updateComic(Long comicId, Comic comicDetails);

    List<Comic> findByTitleAndNotDeleted(String title);

    void deleteComic(Long comicId);
}
