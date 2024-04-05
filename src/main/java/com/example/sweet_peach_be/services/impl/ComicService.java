

package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.exceptions.ResourceNotFoundException;
import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.reposittory.ComicRepository;
import com.example.sweet_peach_be.services.IComicService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ComicService implements IComicService {
    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public List<Comic> getAllComics() {
        return this.comicRepository.findByIsDeletedFalse();
    }

    public Optional<Comic> getComicById(Long comicId) {
        return this.comicRepository.findByIdAndIsDeletedFalse(comicId);
    }

    public Comic createComic(Comic comic) {
        return (Comic)this.comicRepository.save(comic);
    }

    public Comic updateComic(Long comicId, Comic comicDetails) {
        Comic comic = (Comic)this.comicRepository.findByIdAndIsDeletedFalse(comicId).orElseThrow(() -> {
            return new ResourceNotFoundException("User not found with id " + comicId);
        });
        comic.setTitle(comicDetails.getTitle());
        if (comicDetails.getCoverImage() != null) {
            comic.setCoverImage(comicDetails.getCoverImage());
        }

        comic.setDescription(comicDetails.getDescription());
        comic.setViewCount(comicDetails.getViewCount());
        comic.setFollowCount(comicDetails.getFollowCount());
        comic.setRating(comicDetails.getRating());
        comic.setStatus(comicDetails.getStatus());
        return (Comic)this.comicRepository.save(comic);
    }

    public List<Comic> findByTitleAndNotDeleted(String title) {
        return this.comicRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(title);
    }

    public void deleteComic(Long comicId) {
        Comic comic = (Comic)this.comicRepository.findByIdAndIsDeletedFalse(comicId).orElseThrow(() -> {
            return new ResourceNotFoundException("Comic not found with ID: " + comicId);
        });
        comic.setDeleted(true);
        this.comicRepository.save(comic);
    }
}
