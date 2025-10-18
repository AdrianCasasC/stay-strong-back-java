package com.example.demo.controllers;

import com.example.demo.dtos.CorporalWeight;
import com.example.demo.dtos.PrevNextDto;
import com.example.demo.models.Calendar;
import com.example.demo.models.Day;
import com.example.demo.models.PrevNext;
import com.example.demo.services.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping
    public Calendar create(@RequestBody Calendar calendar) {
        return service.create(calendar);
    }

    @GetMapping("/day-detail/{dayId}")
    public ResponseEntity<?> getDay(@PathVariable String dayId) {
        return service.getDay(dayId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @GetMapping("/prev-next")
    public ResponseEntity<?> getCalendarWindow(
            @RequestParam int year,
            @RequestParam int month
    ) {
        try {
            return ResponseEntity.ok(service.getCurrPrevNext(year, month));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error occurred"));
        }
    }

    @GetMapping("/corporal-weight")
    public ResponseEntity<?> getCorporalWeight() {
        try {
            List<CorporalWeight> corporalWeights = service.getCorporalWeight();
            return ResponseEntity.ok(corporalWeights);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error occurred"));
        }
    }

    @GetMapping("/year/{year}")
    public List<Calendar> getAllByYear(@PathVariable Integer year) {
        return service.getAllByYear(year);
    }

    @GetMapping("/month/{month}")
    public List<Calendar> getAllByMonth(@PathVariable Integer month) {
        return service.getAllByMonth(month);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getByYear(@PathVariable String id) {
        return service.getById(id)
                .<ResponseEntity<Calendar>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



}
