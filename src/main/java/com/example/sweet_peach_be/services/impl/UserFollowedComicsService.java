package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.UserFollowedComics;
import com.example.sweet_peach_be.repositories.UserFollowedComicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFollowedComicsService implements IUserFollowedComicsService {

    private final UserFollowedComicsRepository userFollowedComicsRepository;

    @Autowired
    public UserFollowedComicsService(UserFollowedComicsRepository userFollowedComicsRepository) {
        this.userFollowedComicsRepository = userFollowedComicsRepository;
    }

    @Override
    public List<UserFollowedComics> getFollowedComicsByUserId(Long userId) {
        return userFollowedComicsRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    @Override
    public void followComic(Long userId, Long comicId) {
        Optional<UserFollowedComics> existingFollow = userFollowedComicsRepository.findByUserIdAndComicId(userId, comicId);
        if (!existingFollow.isPresent()) {
            UserFollowedComics userFollowedComics = new UserFollowedComics();
            // Set userId and comicId
            userFollowedComicsRepository.save(userFollowedComics);
        }
    }

    @Override
    public void unfollowComic(Long userId, Long comicId) {
        Optional<UserFollowedComics> existingFollow = userFollowedComicsRepository.findByUserIdAndComicId(userId, comicId);
        existingFollow.ifPresent(userFollowedComics -> {
            userFollowedComics.setDeleted(true);
            userFollowedComicsRepository.save(userFollowedComics);
        });
    }
}
