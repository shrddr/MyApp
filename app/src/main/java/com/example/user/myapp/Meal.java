package com.example.user.myapp;

class Meal {

    public int id;
    public String name;
    public String date;
    public String time;
    public int size;

    public Meal(int id, String name, String date, String time, int size) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.size = size;
    }

    public String toString() {
        return id + " " + date + " " + time + " " + name + " " + size;
    }
}
