package com.example.demo.services;

import com.example.demo.models.Calendar;
import com.example.demo.models.Day;
import com.example.demo.repositories.CalendarRepositoryInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CalendarService {

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
