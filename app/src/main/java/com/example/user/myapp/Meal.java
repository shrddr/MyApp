package com.example.user.myapp;

public class Meal {

    public int id;
    public String name;

    public Meal(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return id + " - " + name;
    }
}
