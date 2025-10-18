package com.example.demo.dtos;

import com.example.demo.models.Calendar;

import java.util.List;
import java.util.Map;

public record PrevNextDto(
        Map<String, List<Calendar>> calendars
) {
}

