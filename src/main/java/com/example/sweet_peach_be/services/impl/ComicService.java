package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.models.Chapter;
import com.example.sweet_peach_be.models.Comic;
import com.example.sweet_peach_be.models.ViewCountStatistics;
import com.example.sweet_peach_be.repositories.ChapterRepository;
import com.example.sweet_peach_be.repositories.ComicRepository;
import com.example.sweet_peach_be.repositories.ViewCountStatisticsRepository;
import com.example.sweet_peach_be.services.IComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ComicService implements IComicService {
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ViewCountStatisticsRepository viewCountStatisticsRepository;

    @Override
    public List<Comic> getAllComics() {
        return comicRepository.findAllByIsDeletedFalse();
    }



    @Override
    public Comic getComicByTitle(String title) {
        return (Comic) comicRepository.findByIsDeletedFalseAndTitle(title).orElse(null);
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
    public Comic getComicById(Long id) {
        return comicRepository.findById(id).orElse(null);
    }

}