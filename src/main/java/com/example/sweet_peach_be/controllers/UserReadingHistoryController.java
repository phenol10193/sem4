package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.User;
import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.UserReadingHistory;
import com.example.sweet_peach_be.services.IUserReadingHistoryService;
import com.example.sweet_peach_be.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reading-history")
@CrossOrigin
public class UserReadingHistoryController {

    private final IUserReadingHistoryService userReadingHistoryService;
    private final IUserService userService;
    public UserReadingHistoryController(IUserReadingHistoryService userReadingHistoryService,IUserService userService) {
        this.userReadingHistoryService = userReadingHistoryService;
        this.userService=userService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReadingHistory>> getUserReadingHistoryByUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<UserReadingHistory> userReadingHistoryList = userReadingHistoryService.getUserReadingHistoryByUser(user);
            return ResponseEntity.ok(userReadingHistoryList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}/comic/{comicId}")
    public ResponseEntity<List<Chapter>> getChaptersReadByUserAndComic(
            @PathVariable Long userId, @PathVariable Long comicId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Chapter> chaptersReadByUserAndComic = userReadingHistoryService.getChaptersReadByUserAndComic(user, comicId);
            return ResponseEntity.ok(chaptersReadByUserAndComic);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/Create")
    public ResponseEntity<String> addUserReadingHistory(@RequestBody UserReadingHistory userReadingHistory) {
        Optional<User> userOptional = userService.getUserById(userReadingHistory.getUser().getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userReadingHistoryService.addUserReadingHistory(user, userReadingHistory.getComic().getId(), userReadingHistory.getChapter().getId());
            return ResponseEntity.ok("User reading history added successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/{userId}/comic/{comicId}/delete")
    public ResponseEntity<String> deleteReadingHistory(@PathVariable Long userId,
                                                       @PathVariable Long comicId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userReadingHistoryService.deleteUserReadingHistory(user, comicId);
            return ResponseEntity.ok("User reading history deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{historyId}/read-status")
    public ResponseEntity<String> updateReadStatus(@PathVariable Long historyId,
                                                   @RequestParam boolean isRead) {
        userReadingHistoryService.updateReadStatus(historyId, isRead);
        return ResponseEntity.ok("Read status updated successfully");
    }
}