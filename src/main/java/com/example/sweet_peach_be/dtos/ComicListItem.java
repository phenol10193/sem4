package com.example.sweet_peach_be.dtos;

public class ComicListItem {
    private Long id;
    private String coverImage;
    private String title;
    private int viewCount;
    private int followCount;
    private String latestChapterTitle;
    private String timeSinceLastUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public String getLatestChapterTitle() {
        return latestChapterTitle;
    }

    public void setLatestChapterTitle(String latestChapterTitle) {
        this.latestChapterTitle = latestChapterTitle;
    }

    public String getTimeSinceLastUpdate() {
        return timeSinceLastUpdate;
    }

    public void setTimeSinceLastUpdate(String timeSinceLastUpdate) {
        this.timeSinceLastUpdate = timeSinceLastUpdate;
    }

}

