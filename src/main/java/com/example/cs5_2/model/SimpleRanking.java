package com.example.cs5_2.model;

public class SimpleRanking {
	private String name;
    private double value;

    public SimpleRanking(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

}
