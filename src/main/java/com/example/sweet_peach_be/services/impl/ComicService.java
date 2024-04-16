package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.dtos.ComicListItem;
import com.example.sweet_peach_be.models.*;
import com.example.sweet_peach_be.repositories.ChapterRepository;
import com.example.sweet_peach_be.repositories.ComicRepository;
import com.example.sweet_peach_be.repositories.UserFollowedComicRepository;
import com.example.sweet_peach_be.repositories.ViewCountStatisticsRepository;
import com.example.sweet_peach_be.services.IComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ComicService implements IComicService {
    private final ComicRepository comicRepository;
    private final ChapterRepository chapterRepository;
    private final ViewCountStatisticsRepository viewCountStatisticsRepository;
    private final UserReadingHistoryService userReadingHistoryService;


    private final UserFollowedComicRepository userFollowedComicRepository ;
    @Autowired
    public ComicService(ComicRepository comicRepository, ChapterRepository chapterRepository, ViewCountStatisticsRepository viewCountStatisticsRepository, UserReadingHistoryService userReadingHistoryService,UserFollowedComicRepository userFollowedComicRepository) {
        this.comicRepository = comicRepository;
        this.chapterRepository = chapterRepository;
        this.viewCountStatisticsRepository = viewCountStatisticsRepository;
        this.userReadingHistoryService = userReadingHistoryService;
        this.userFollowedComicRepository = userFollowedComicRepository;

    }
    @Override
    public List<Comic> getAllComics() {
        return comicRepository.findAllByIsDeletedFalse();
    }

    @Override
    public Comic createComic(Comic comic) {
        return comicRepository.save(comic);
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
    public void deleteComic(Long id) {
        Comic comic = comicRepository.findById(id).orElse(null);
        if (comic != null) {
            comic.setDeleted(true);
            comicRepository.save(comic);
        }
    }
    @Override
    public List<Comic> getNewestComics(int limit) {
        List<Chapter> newestChapters = chapterRepository.findTopChaptersOrderByUpdatedAtDesc(limit);
        List<Long> comicIds = newestChapters.stream().map(Chapter::getComicId).limit(limit).distinct().collect(Collectors.toList());
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
        List<Comic> allComic=getAllComics();
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
        List<ComicListItem> hotComicItems = new ArrayList<>();
        for (Comic comic : hotComics) {
            ComicListItem item = mapComicToItem(comic);
            List<Genre> genres = new ArrayList<>(comic.getGenres());
            item.setGenres(genres);
            hotComicItems.add(item);
        }
        return hotComicItems;
    }
    public List<UserFollowedComics> getFollowedComicsByUserId(Long userId) {
        return userFollowedComicRepository.findByUserIdAndIsDeletedFalse(userId);
    }
    @Override
    public List<ComicListItem> getFollowedComicsByUserIdItem(Long userId) {
        List<UserFollowedComics> followedComics = getFollowedComicsByUserId(userId);
        List<ComicListItem> followedComicItems = new ArrayList<>();

        for (UserFollowedComics followedComic : followedComics) {
            Long comicId = followedComic.getComicId();
            Comic comic = comicRepository.findById(comicId).orElse(null);

            if (comic != null) {
                // Nếu Comic tồn tại, chuyển đổi nó thành ComicListItem và thêm vào danh sách
                ComicListItem item = mapComicToItem(comic);
                followedComicItems.add(item);
            }
        }

        return followedComicItems;
    }

    @Override
    public List<ComicListItem> getComicHistory(Long userId) {
        List<UserReadingHistory> readingHistory = userReadingHistoryService.getReadingHistory(userId);
        Map<Long, Long> latestChapterIds = new HashMap<>();

        // Lặp qua lịch sử đọc của người dùng để lấy chapter gần đây nhất của mỗi truyện
        for (UserReadingHistory history : readingHistory) {
            Long comicId = history.getComicId();
            Long chapterId = history.getChapterId();

            // Nếu truyện chưa có chapter gần đây nhất hoặc chapter hiện tại mới hơn
            if (!latestChapterIds.containsKey(comicId) || history.getTimestamp().isAfter(userReadingHistoryService.getLatestReadTimestamp(userId, comicId))) {
                latestChapterIds.put(comicId, chapterId);
            }
        }

        // Lấy thông tin của chapter gần đây nhất của mỗi truyện và chuyển đổi sang định dạng ComicListItem
        List<ComicListItem> comicHistory = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : latestChapterIds.entrySet()) {
            Long comicId = entry.getKey();
            Long chapterId = entry.getValue();

            Comic comic = comicRepository.findById(comicId).orElse(null);
            Chapter latestChapter = chapterRepository.findById(chapterId).orElse(null);

            if (comic != null && latestChapter != null) {
                ComicListItem item = mapComicToItem(comic);
                item.setLatestChapterTitle(latestChapter.getTitle());
                item.setTimeSinceLastUpdate(calculateTimeSinceLastUpdate(latestChapter.getUpdatedAt()));
                comicHistory.add(item);
            }
        }

        return comicHistory;
    }
    @Override
    public List<ComicListItem> getLocalStorageItem(List<Map<String, Long>> comicChapterList) {
        List<ComicListItem> localStorageItems = new ArrayList<>();

        for (Map<String, Long> comicChapter : comicChapterList) {
            Long comicId = comicChapter.get("comicId");
            Long chapterId = comicChapter.get("chapterId");

            Comic comic = comicRepository.findById(comicId).orElse(null);
            Chapter chapter = chapterRepository.findById(chapterId).orElse(null);

            if (comic != null && chapter != null) {
                ComicListItem item = mapComicToItem(comic);
                item.setLatestChapterTitle(chapter.getTitle());
                item.setTimeSinceLastUpdate(calculateTimeSinceLastUpdate(chapter.getUpdatedAt()));
                localStorageItems.add(item);
            }
        }

        return localStorageItems;
    }

    @Override
    public List<ComicListItem> getComicItemsByGenreId(Long genreId) {
        List<Comic> comicsByGenreId = getComicsByGenreId(genreId);
        return comicsByGenreId.stream()
                .map(this::mapComicToItem)
                .collect(Collectors.toList());
    }

    private ComicListItem mapComicToItem(Comic comic) {
        ComicListItem item = new ComicListItem();
        item.setId(comic.getId());
        item.setCoverImage(comic.getCoverImage());
        item.setTitle(comic.getTitle());
        item.setViewCount(comic.getViewCount());
        item.setFollowCount(comic.getFollowCount());
        item.setStatus(comic.getStatus());
        Chapter latestChapter = chapterRepository.findFirstByComicIdOrderByChapterNumberDesc(comic.getId());
        if (latestChapter != null) {
            item.setLatestChapterTitle(latestChapter.getTitle());
            item.setTimeSinceLastUpdate(calculateTimeSinceLastUpdate(latestChapter.getUpdatedAt()));
        }
        return item;
    }

    public String calculateTimeSinceLastUpdate(LocalDateTime updatedAt) {
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
    public void incrementFollowCount(Long comicId) {
        Comic comic = comicRepository.findById(comicId).orElse(null);
        if (comic != null) {
            comic.setFollowCount(comic.getFollowCount() + 1);
            comicRepository.save(comic);
        }
    }

    public void decrementFollowCount(Long comicId) {
        Comic comic = comicRepository.findById(comicId).orElse(null);
        if (comic != null) {
            int followCount = comic.getFollowCount();
            if (followCount > 0) {
                comic.setFollowCount(followCount - 1);
                comicRepository.save(comic);
            }
        }
    }

}