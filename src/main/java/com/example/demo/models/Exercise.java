package com.example.demo.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Exercise {
    private String name;
    private List<Series> series = new ArrayList<>();
}
