package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.Comic;
import java.util.List;

public interface IComicService {
    List<Comic> getAllComics();
    Comic getComicByTitle(String title);

    Comic createComic(Comic comic);
    Comic updateComic(Long id, Comic comic);
    void deleteComic(Long id);

    List<Comic> getNewestComics(int limit);

    List<Comic> getHotComics(String period, int limit);

    Comic getComicById(Long id);
}
