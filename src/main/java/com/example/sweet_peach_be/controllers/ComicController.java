
package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.services.IComicService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/api/comics"})
public class ComicController {
    private final IComicService comicService;

    @Autowired
    public ComicController(IComicService comicService) {
        this.comicService = comicService;
    }

    @GetMapping
    public ResponseEntity<List<Comic>> getAllComics() {
        List<Comic> comics = this.comicService.getAllComics();
        return new ResponseEntity(comics, HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Comic> getComicById(@PathVariable Long id) {
        Optional<Comic> comic = this.comicService.getComicById(id);
        return (ResponseEntity)comic.map((value) -> {
            return new ResponseEntity(value, HttpStatus.OK);
        }).orElseGet(() -> {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        });
    }

    @PostMapping({"/create"})
    public ResponseEntity<Comic> createComic(@RequestBody Comic comic) {
        Comic createdComic = this.comicService.createComic(comic);
        return new ResponseEntity(createdComic, HttpStatus.CREATED);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comicDetails) {
        Comic updatedComic = this.comicService.updateComic(id, comicDetails);
        return new ResponseEntity(updatedComic, HttpStatus.OK);
    }

    @GetMapping({"/title/{title}"})
    public ResponseEntity<List<Comic>> getComicsByTitle(@PathVariable("title") String title) {
        List<Comic> comics = this.comicService.findByTitleAndNotDeleted(title);
        return new ResponseEntity(comics, HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteComic(@PathVariable Long id) {
        this.comicService.deleteComic(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
