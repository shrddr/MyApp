package com.example.user.myapp;

class Meal {

    public int id;
    public String name;
    public int time;

    public Meal(int id, String name, int time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public String toString() {
        return id + " " + time + " " + name;
    }
}
