package com.example.demo.models;

import com.mongodb.lang.Nullable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Optional;

@Data
public class Day {
    @Field("id")
    private String id;
    private String date;
    private Task tasks;
    private Double weightNumber;
    @Nullable
    private Training training;
}
