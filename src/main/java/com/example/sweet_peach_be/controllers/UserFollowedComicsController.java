package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.UserFollowedComics;
import com.example.sweet_peach_be.services.IUserFollowedComicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/followed-comics")
@CrossOrigin
public class UserFollowedComicsController {

    private final IUserFollowedComicsService userFollowedComicsService;

    @Autowired
    public UserFollowedComicsController(IUserFollowedComicsService userFollowedComicsService) {
        this.userFollowedComicsService = userFollowedComicsService;
    }

    @GetMapping("/{userId}")
    public List<UserFollowedComics> getFollowedComicsByUserId(@PathVariable Long userId) {
        return userFollowedComicsService.getFollowedComicsByUserId(userId);
    }

    @PostMapping("/{userId}/follow/{comicId}")
    public void followComic(@PathVariable Long userId, @PathVariable Long comicId) {
        userFollowedComicsService.followComic(userId, comicId);
    }

    @DeleteMapping("/{userId}/unfollow/{comicId}")
    public void unfollowComic(@PathVariable Long userId, @PathVariable Long comicId) {
        userFollowedComicsService.unfollowComic(userId, comicId);
    }
}
