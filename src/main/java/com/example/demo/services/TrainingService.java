package com.example.demo.services;

import com.example.demo.models.Training;
import com.example.demo.repositories.CalendarRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final CalendarRepositoryInterface repository;

    public Optional<?> editTraining(String dayId, Training training) {
        return repository.findByDaysId(dayId).map(cal -> {
            cal.getDays().forEach(day -> {
                if (day.getId().equals(dayId)) day.setTraining(training);
            });
            repository.save(cal);
            return cal;
        });
    }
}
