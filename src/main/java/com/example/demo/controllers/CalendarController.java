package com.example.demo.controllers;

import com.example.demo.models.Calendar;
import com.example.demo.models.Day;
import com.example.demo.services.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService service;

    public CalendarController(CalendarService service) {
        this.service = service;
    }


    @GetMapping
    public List<Calendar> getAll() {
        return service.getAll();
    }

    @GetMapping("/year/{year}")
    public List<Calendar> getAllByYear(@PathVariable Integer year) {
        return service.getAllByYear(year);
    }

    @GetMapping("/month/{month}")
    public List<Calendar> getAllByMonth(@PathVariable Integer month) {
        return service.getAllByMonth(month);
    }

    @GetMapping("/day-detail/{dayId}")
    public ResponseEntity<?> getDay(@PathVariable String dayId) {
        return service.getDay(dayId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getByYear(@PathVariable String id) {
        return service.getById(id)
                .<ResponseEntity<Calendar>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Calendar create(@RequestBody Calendar calendar) {
        return service.create(calendar);
    }

    @PutMapping("/update-day/{year}/{month}/{dayId}")
    public ResponseEntity<?> updateDay(
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable String dayId,
            @RequestBody Day updatedDay
    ) {
        return service.updateDay(year, month, dayId, updatedDay)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/update-day/{year}/{month}")
    public ResponseEntity<?> addDay(
            @PathVariable int year,
            @PathVariable int month,
            @RequestBody Day newDay
    ) {
        return ResponseEntity.ok(service.addDay(year, month, newDay));
    }
}
