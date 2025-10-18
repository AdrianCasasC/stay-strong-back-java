package com.example.demo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Document(collection = "calendars")
public class Calendar {
    @Id
    private String _id;
    private int year;
    private int month;
    private List<Day> days = new ArrayList<>();
}

