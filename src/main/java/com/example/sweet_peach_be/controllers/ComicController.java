package com.example.sweet_peach_be.controllers;

import com.example.sweet_peach_be.dtos.ComicListItem;
import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.Genre;
import com.example.sweet_peach_be.services.IComicService;
import com.example.sweet_peach_be.services.UploadService;
import com.example.sweet_peach_be.services.impl.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController

@RequestMapping("/api/comics")
@CrossOrigin
public class ComicController {
    @Autowired
    private IComicService comicService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UploadService uploadService;

    @GetMapping("getalls")
    public List<ComicListItem> getAllComicItems() {
        return comicService.getAllComicItems();
    }

    @GetMapping("/newest")
    public ResponseEntity<List<Comic>> getNewestComics(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<Comic> newestComics = comicService.getNewestComics(limit);
        return new ResponseEntity<>(newestComics, HttpStatus.OK);
    }

    @GetMapping("/hot")
    public ResponseEntity<List<Comic>> getHotComics(@RequestParam(name = "period") String period,
                                                    @RequestParam(name = "limit", defaultValue = "6") int limit) {
        List<Comic> hotComics = comicService.getHotComics(period, limit);
        return new ResponseEntity<>(hotComics, HttpStatus.OK);
    }

    @PostMapping("/{comicId}/genres/{genreId}")
    public ResponseEntity<String> addGenreToComic(@PathVariable Long comicId, @PathVariable Long genreId) {
        Comic comic = comicService.getComicById(comicId);
        if (comic == null) {
            return new ResponseEntity<>("Comic not found", HttpStatus.NOT_FOUND);
        }

        Optional<Genre> genre = genreService.getGenreById(genreId);
        if (!genre.isPresent()) {
            return new ResponseEntity<>("Genre not found", HttpStatus.NOT_FOUND);
        }

        comic.addGenre(genre.get());
        comicService.createComic(comic);

        return new ResponseEntity<>("Genre added to Comic successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{comicId}/genres/{genreId}")
    public ResponseEntity<String> deleteGenreFromComic(@PathVariable Long comicId, @PathVariable Long genreId) {
        Comic comic = comicService.getComicById(comicId);
        if (comic == null) {
            return new ResponseEntity<>("Comic not found", HttpStatus.NOT_FOUND);
        }

        Optional<Genre> genre = genreService.getGenreById(genreId);
        if (!genre.isPresent()) {
            return new ResponseEntity<>("Genre not found", HttpStatus.NOT_FOUND);
        }

        if (!comic.getGenres().contains(genre.get())) {
            return new ResponseEntity<>("Genre is not associated with this Comic", HttpStatus.BAD_REQUEST);
        }

        comic.removeGenre(genre.get());
        comicService.createComic(comic);

        return new ResponseEntity<>("Genre removed from Comic successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Comic>> getAllComics() {
        List<Comic> comics = comicService.getAllComics();
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Comic> getComicById(@PathVariable Long id) {
        Optional<Comic> optionalComic = Optional.ofNullable(comicService.getComicById(id));
        return optionalComic.map(comic -> new ResponseEntity<>(comic, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("{id}/chapters")
    public ResponseEntity<List<Chapter>> getChaptersByComicId(@PathVariable Long id) {
        List<Chapter> chapters = comicService.getChaptersByComicId(id);
        return chapters != null ? new ResponseEntity<>(chapters, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<Comic> createComic(@RequestParam("title") String title,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("description") String description,
                                             @RequestParam("status") String status,
                                             @RequestParam("genres") List<Long> genreIds) {
        try {
            // Lưu ảnh và nhận đường dẫn
            String coverImage = uploadService.storeImage(file);

            // Tạo mới đối tượng Comic và lưu vào cơ sở dữ liệu
            Comic comic = new Comic();
            comic.setTitle(title);
            comic.setDescription(description);
            comic.setStatus(status);
            comic.setCoverImage(coverImage);

            // Gán danh sách ID thể loại cho Comic
            comic.setGenres(genreIds);

            Comic createdComic = comicService.createComic(comic);

            return new ResponseEntity<>(createdComic, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("title") String title,
                                             @RequestParam("description") String description,
                                             @RequestParam("status") String status) {
        try {
            // Lưu ảnh và nhận đường dẫn
            String coverImage = uploadService.storeImage(file);

            // Lấy thông tin truyện cũ từ cơ sở dữ liệu
            Comic existingComic = comicService.getComicById(id);
            if (existingComic == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Cập nhật thông tin truyện và lưu vào cơ sở dữ liệu
            existingComic.setTitle(title);
            existingComic.setDescription(description);
            existingComic.setStatus(status);
            existingComic.setCoverImage(coverImage);
            Comic updatedComic = comicService.updateComic(id, existingComic);

            return new ResponseEntity<>(updatedComic, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComic(@PathVariable Long id) {
        comicService.deleteComic(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/newest1")
    public List<ComicListItem> getNewestComicsv1(@RequestParam int limit) {
        return comicService.getNewestComicItems(limit);
    }

    @GetMapping("/hot1")
    public List<ComicListItem> getHotComicsv1(@RequestParam String period, @RequestParam int limit) {
        return comicService.getHotComicItems(period, limit);
    }

    @GetMapping("/genre1/{genreId}")
    public List<ComicListItem> getComicsByGenreIdv1(@RequestParam Long genreId) {
        return comicService.getComicItemsByGenreId(genreId);

    }
}