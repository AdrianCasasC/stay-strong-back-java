package com.example.demo.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Training {
    private String name;
    private List<Exercise> exercises = new ArrayList<>();
}
