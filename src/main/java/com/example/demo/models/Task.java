package com.example.demo.models;

import lombok.Data;

@Data
public class Task {
    private boolean steps;
    private boolean calories;
    private boolean training;
    private boolean supplementation;
    private boolean weight;
}
