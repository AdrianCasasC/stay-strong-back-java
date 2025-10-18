package com.example.demo.services;

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

import java.util.*;


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

    public PrevNext getCurrPrevNext(int y, int m) {
        record MonthTarget(int year, int month) {}
        List<MonthTarget> monthTargets = List.of(
                m == 1 ? new MonthTarget(y - 1, 12) : new MonthTarget(y, m - 1),
                new MonthTarget(y, m),
                m == 12 ? new MonthTarget(y + 1, 1) : new MonthTarget(y, m + 1)
        );

        try (var mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase db = mongoClient.getDatabase(mongoDbName);
            MongoCollection<Document> collection = db.getCollection("calendars");

            // Build $or query
            var orQuery = monthTargets.stream()
                    .map(mt -> new Document("year", mt.year()).append("month", mt.month()))
                    .toList();

            var query = new Document("$or", orQuery);

            // Execute query
            List<Document> calendars = collection.find(query).into(new java.util.ArrayList<>());


            Document previous = calendars.stream()
                    .filter(cal -> cal.getInteger("year") == monthTargets.getFirst().year()
                            && cal.getInteger("month") == monthTargets.getFirst().month())
                    .findFirst()
                    .orElse(null);

            Document current = calendars.stream()
                    .filter(cal -> cal.getInteger("year") == monthTargets.get(1).year()
                            && cal.getInteger("month") == monthTargets.get(1).month())
                    .findFirst()
                    .orElse(null);

            Document next = calendars.stream()
                    .filter(cal -> cal.getInteger("year") == monthTargets.getLast().year()
                            && cal.getInteger("month") == monthTargets.getLast().month())
                    .findFirst()
                    .orElse(null);

            return new PrevNext(previous, current, next);

        } catch (Exception e) {
            throw new RuntimeException("Error getting current, previous, and next months");
        }
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
