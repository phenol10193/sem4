package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.UserFollowedComics;

import java.util.List;

public interface IUserFollowedComicsService {
    List<UserFollowedComics> getFollowedComicsByUserId(Long userId);
    void followComic(Long userId, Long comicId);
    void unfollowComic(Long userId, Long comicId);
}
