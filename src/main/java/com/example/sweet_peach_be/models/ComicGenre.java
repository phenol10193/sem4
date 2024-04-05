package com.example.sweet_peach_be.models;
import jakarta.persistence.*;
@Entity
@Table(name = "comic_genres")
public class ComicGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comic_id")
    private Comic comic;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    // Getters and setters
}
