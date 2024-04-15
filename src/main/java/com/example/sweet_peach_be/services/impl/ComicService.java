package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.dtos.ComicListItem;
import com.example.sweet_peach_be.exceptions.ResourceNotFoundException;
import com.example.sweet_peach_be.models.*;
import com.example.sweet_peach_be.repositories.*;
import com.example.sweet_peach_be.services.IComicService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ComicService implements IComicService {
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ViewCountStatisticsRepository viewCountStatisticsRepository;
    @Autowired
    private UserReadingHistoryService userReadingHistoryService;
    @Autowired
    private ComicGenreRepository comicGenreRepository;

    @Override
    public List<Comic> getAllComics() {
        return comicRepository.findAllByIsDeletedFalse();
    }

    @Override
    public Comic createComic(Comic comic) {
        return comicRepository.save(comic);
    }
    @Transactional
    @Override
    public void insertComicWithGenres(Comic comic, List<Long> genreIds) {
        // Lưu thông tin Comic
        Comic savedComic = comicRepository.save(comic);

        // Tạo và lưu thông tin ComicGenre
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found with ID: " + genreId));
            ComicGenre comicGenre = new ComicGenre();
            comicGenre.setComic(savedComic); // Sử dụng đối tượng Comic đã được lưu
            comicGenre.setGenre(genre); // Thiết lập đối tượng Genre cho ComicGenre
            comicGenreRepository.save(comicGenre);
        }
    }

    @Override
    public Comic updateComic(Long id, Comic comic) {
        comic.setId(id);
        return comicRepository.save(comic);
    }

    @Override
    public List<Chapter> getChaptersByComicId(Long id) {
        Optional<Comic> optionalComic = comicRepository.findById(id);
        if (optionalComic.isPresent()) {
            Comic comic = optionalComic.get();
            return comic.getChapters();
        } else {
            return null; // Hoặc throw exception nếu cần
        }
    }
    @Override
    public List<Genre> getGenresByComicId(Long comicId) {
        Optional<Comic> optionalComic = comicRepository.findById(comicId);
        if (optionalComic.isPresent()) {
            Comic comic = optionalComic.get();
            List<Genre> genreList = new ArrayList<>(comic.getGenres());
            return genreList;
        }
        return new ArrayList<>(); // Trả về danh sách rỗng nếu không tìm thấy truyện
    }
    @Override
    public void deleteComic(Long id) {
        Optional<Comic> optionalComic = comicRepository.findById(id);
        optionalComic.ifPresent(comic -> {
            comic.setDeleted(true);
            comicRepository.save(comic);
        });
    }

    @Override
    public List<Comic> getNewestComics(int limit) {
        List<Chapter> newestChapters = chapterRepository.findTopChaptersOrderByUpdatedAtDesc(limit);
        List<Long> comicIds = newestChapters.stream().map(Chapter::getComicId).distinct().collect(Collectors.toList());
        return comicRepository.findAllById(comicIds);
    }

    @Override
    public List<Comic> getHotComics(String period, int limit) {
        LocalDate startDate = switch (period) {
            case "day" -> LocalDate.now().minusDays(1);
            case "week" -> LocalDate.now().minusWeeks(1);
            case "month" -> LocalDate.now().minusMonths(1);
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        };

        // Tính tổng số lượt xem của mỗi truyện trong khoảng thời gian được chỉ định
        Map<Long, Integer> comicViewCounts = new HashMap<>();
        List<ViewCountStatistics> viewCounts = viewCountStatisticsRepository.findByViewDateAfter(startDate);
        for (ViewCountStatistics statistics : viewCounts) {
            Long comicId = statistics.getComicId();
            Integer viewCount = comicViewCounts.getOrDefault(comicId, 0);
            viewCount += statistics.getViewCount();
            comicViewCounts.put(comicId, viewCount);
        }
        // In kết quả tổng số lượt xem của mỗi truyện
        for (Map.Entry<Long, Integer> entry : comicViewCounts.entrySet()) {
            Long comicId = entry.getKey();
            Integer viewCount = entry.getValue();
            System.out.println("Comic ID: " + comicId + ", Total View Count: " + viewCount);
        }
        // Sắp xếp các truyện dựa trên tổng số lượt xem và chỉ lấy ra số lượng truyện cần thiết
        List<Long> hotComicIds = comicViewCounts.entrySet().stream()
                .sorted((c1, c2) -> c2.getValue().compareTo(c1.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Lấy danh sách các truyện theo hotComicIds và sắp xếp theo thứ tự của hotComicIds
        List<Comic> hotComics = comicRepository.findAllById(hotComicIds);
        Map<Long, Comic> comicMap = hotComics.stream().collect(Collectors.toMap(Comic::getId, Function.identity()));

        return hotComicIds.stream().map(comicMap::get).collect(Collectors.toList());
    }

    @Override
    public List<Comic> getComicsByGenreId(Long genreId) {
        return comicRepository.findByIsDeletedFalseAndGenresId(genreId);
    }

    @Override
    public Comic getComicById(Long id) {
        return comicRepository.findById(id).orElse(null);
    }

    @Override
    public List<ComicListItem> getAllComicItems() {
        List<Comic> allComic = getAllComics();
        return allComic.stream()
                .map(this::mapComicToItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComicListItem> getNewestComicItems(int limit) {
        List<Comic> newestComics = getNewestComics(limit);
        return newestComics.stream()
                .map(this::mapComicToItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComicListItem> getHotComicItems(String period, int limit) {
        List<Comic> hotComics = getHotComics(period, limit);
        return hotComics.stream()
                .map(this::mapComicToItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComicListItem> getComicItemsByGenreId(Long genreId) {
        List<Comic> comicsByGenreId = getComicsByGenreId(genreId);
        return comicsByGenreId.stream()
                .map(this::mapComicToItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comic> getReadComicsByUserId(Long userId) {
        // Lấy danh sách lịch sử đọc của người dùng
        List<UserReadingHistory> readingHistory = userReadingHistoryService.getReadingHistory(userId);

        // Lấy danh sách truyện đã đọc từ danh sách lịch sử
        List<Comic> readComics = new ArrayList<>();
        for (UserReadingHistory history : readingHistory) {
            Comic comic = comicRepository.findById(history.getComicId()).orElse(null);
            if (comic != null) {
                readComics.add(comic);
            }
        }

        return readComics;
    }

    @Override
    public List<ComicListItem> getComicHistory(Long userId) {
        List<Comic> comicsHistory = getReadComicsByUserId(userId);
        return comicsHistory.stream()
                .map(this::mapComicToItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addGenreToComic(Long comicId, Long genreId) {
        Optional<Comic> optionalComic = comicRepository.findById(comicId);
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);

        if (optionalComic.isPresent() && optionalGenre.isPresent()) {
            Comic comic = optionalComic.get();
            Genre genre = optionalGenre.get();

            comic.getGenres().add(genre);
            genre.getComics().add(comic);

            comicRepository.save(comic);
            genreRepository.save(genre);
        } else {
            throw new EntityNotFoundException("Comic or Genre not found");
        }
    }

    @Override
    @Transactional
    public void removeGenreFromComic(Long comicId, Long genreId) {
        Optional<Comic> optionalComic = comicRepository.findById(comicId);
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);

        if (optionalComic.isPresent() && optionalGenre.isPresent()) {
            Comic comic = optionalComic.get();
            Genre genre = optionalGenre.get();

            comic.getGenres().remove(genre);
            genre.getComics().remove(comic);

            comicRepository.save(comic);
            genreRepository.save(genre);
        } else {
            throw new EntityNotFoundException("Comic or Genre not found");
        }
    }

    private ComicListItem mapComicToItem(Comic comic) {
        ComicListItem item = new ComicListItem();
        item.setId(comic.getId());
        item.setCoverImage(comic.getCoverImage());
        item.setTitle(comic.getTitle());
        item.setViewCount(comic.getViewCount());
        item.setFollowCount(comic.getFollowCount());
        Chapter latestChapter = chapterRepository.findFirstByComicIdOrderByChapterNumberDesc(comic.getId());
        if (latestChapter != null) {
            item.setLatestChapterTitle(latestChapter.getTitle());
            item.setTimeSinceLastUpdate(calculateTimeSinceLastUpdate(latestChapter.getUpdatedAt()));
        }
        return item;
    }

    private String calculateTimeSinceLastUpdate(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(updatedAt, now);
        long hours = duration.toHours();
        if (hours < 24) {
            return hours + "h";
        } else {
            long days = duration.toDays();
            return days + "d";
        }
    }
}
