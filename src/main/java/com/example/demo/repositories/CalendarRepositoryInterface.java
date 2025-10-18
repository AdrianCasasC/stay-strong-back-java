package com.example.demo.repositories;

import com.example.demo.models.Calendar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepositoryInterface extends MongoRepository<Calendar, String> {
    Optional<Calendar> findByYearAndMonth(int year, int month);
    List<Calendar> findByYear(Integer year);
    Optional<Calendar> findByDaysId(String dayId);
    List<Calendar> findByMonth(Integer month);
    List<Calendar> findByYearInAndMonthIn(List<Integer> years, List<Integer> months);
}
