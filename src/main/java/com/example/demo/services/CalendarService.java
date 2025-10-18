package com.example.demo.services;

import com.example.demo.dtos.CorporalWeight;
import com.example.demo.dtos.MonthYear;
import com.example.demo.dtos.PrevNextDto;
import com.example.demo.models.Calendar;
import com.example.demo.models.Day;
import com.example.demo.models.PrevNext;
import com.example.demo.repositories.CalendarRepositoryInterface;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CalendarService {

    @Value("${MONGO_URI}")
    private String mongoUri;

    @Value("${MONGO_DB_NAME}")
    private String mongoDbName;

    @Value("${MONGO_DB_NAME}")
    private String mongoCollName;


    private final CalendarRepositoryInterface repository;

    public CalendarService(CalendarRepositoryInterface repository) {
        this.repository = repository;
    }

    public List<Calendar> getAll() {
        List<Calendar> calendars = repository.findAll();
        return repository.findAll();
    }

    public List<Calendar> getAllByYear(Integer year) {
        List<Calendar> calendars = repository.findByYear(year);
        return repository.findByYear(year);
    }

    public PrevNextDto getCurrPrevNext(int year, int month) {
        List<MonthYear> targets = getMonthYears(year, month);

        // Query MongoDB
        Set<Integer> years = targets.stream().map(MonthYear::year).collect(Collectors.toSet());
        Set<Integer> months = targets.stream().map(MonthYear::month).collect(Collectors.toSet());

        List<Calendar> results = repository.findByYearInAndMonthIn(new ArrayList<>(years), new ArrayList<>(months));

        // Group results by exact match
        Map<String, Calendar> grouped = new HashMap<>();
        for (String key : List.of("previous", "current", "next")) {
            grouped.put(key, new Calendar());
        }

        for (Calendar c : results) {
            for (int i = 0; i < targets.size(); i++) {
                MonthYear my = targets.get(i);
                if (c.getYear() == my.year() && c.getMonth() == my.month()) {
                    String key = switch (i) {
                        case 0 -> "previous";
                        case 1 -> "current";
                        case 2 -> "next";
                        default -> "unknown";
                    };
                   grouped.put(key, Calendar.builder()
                           ._id(c.get_id())
                           .year(c.getYear())
                           .month(c.getMonth())
                           .days(c.getDays()).build());
                    break;
                }
            }
        }

        return new PrevNextDto(grouped);
    }

    public List<CorporalWeight> getCorporalWeight() {
        List<Calendar> calendars = repository.findAll();

        return calendars.stream()
                .flatMap(c -> c.getDays().stream())
                .filter(day -> day.getWeightNumber() != null)
                .map(day -> new CorporalWeight(day.getDate(), day.getWeightNumber()))
                .collect(Collectors.toList());
    }

    private static List<MonthYear> getMonthYears(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        // Compute prev/current/next
        YearMonth current = YearMonth.of(year, month);
        YearMonth previous = current.minusMonths(1);
        YearMonth next = current.plusMonths(1);

        return List.of(
                new MonthYear(previous.getYear(), previous.getMonthValue()),
                new MonthYear(current.getYear(), current.getMonthValue()),
                new MonthYear(next.getYear(), next.getMonthValue())
        );
    }

    public List<Calendar> getAllByMonth(Integer month) {
        List<Calendar> calendars = repository.findByMonth(month);
        return repository.findByMonth(month);
    }

    public List<Calendar> getByYear(Integer year) {
        List<Calendar> calendars = repository.findByYear(year);
        return repository.findAll();
    }

    public Optional<Day> getDay(String dayId) {
        // Optional<Calendar> foundCalendar = repository.findByDaysId(dayId);
        return repository.findByDaysId(dayId)
                .flatMap(calendar -> calendar.getDays().stream()
                        .filter(d -> d.getId().equals(dayId))
                        .findFirst());
    }

    public Optional<Calendar> getById(String id) {
        Optional<Calendar> findById = repository.findById(id);
        return repository.findById(id);
    }

    public Calendar create(Calendar calendar) {
        return repository.save(calendar);
    }

    public Optional<Day> updateDay(int year, int month, String dayId, Day updatedDay) {
        return repository.findByYearAndMonth(year, month).map(cal -> {
            cal.getDays().removeIf(d -> d.getId().equals(dayId));
            cal.getDays().add(updatedDay);
            repository.save(cal);
            return updatedDay;
        });
    }

    public Calendar addDay(int year, int month, Day newDay) {
        var cal = repository.findByYearAndMonth(year, month)
                .orElseGet(() -> {
                    Calendar c = new Calendar();
                    c.setYear(year);
                    c.setMonth(month);
                    c.setDays(new ArrayList<>());
                    return c;
                });
        newDay.setId(UUID.randomUUID().toString());
        cal.getDays().add(newDay);
        return repository.save(cal);
    }
}
