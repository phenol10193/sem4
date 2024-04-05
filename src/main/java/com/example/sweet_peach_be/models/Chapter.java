package com.example.sweet_peach_be.models;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @Column(name = "chapter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comic_id")
    private Comic comic;
    @Column(name = "comic_id", insertable = false, updatable = false)
    private Long comicId;
    private String title;
    private int chapterNumber;
    private int viewCount;
    private boolean isDeleted;
    private Timestamp updatedAt;
    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComic() {
        return comic.getTitle();
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public Long getComicId() {
        return comicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }



}

