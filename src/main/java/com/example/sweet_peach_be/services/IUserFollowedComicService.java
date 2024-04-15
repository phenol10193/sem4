package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.UserFollowedComics;

import java.util.List;

public interface IUserFollowedComicService {


    UserFollowedComics followComic(Long userId, Long comicId);

    void unfollowComic(Long userId, Long comicId);
}
