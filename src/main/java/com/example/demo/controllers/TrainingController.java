package com.example.demo.controllers;


import com.example.demo.models.Training;
import com.example.demo.services.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar/day-detail/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService service;

    @PutMapping("/{dayId}")
    public ResponseEntity<?> editTraining(@PathVariable String dayId, @RequestBody Training training) {
        return service.editTraining(dayId, training)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
