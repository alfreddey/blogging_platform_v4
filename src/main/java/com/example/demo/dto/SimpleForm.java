package com.example.demo.dto;

public class SimpleForm {
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return "SimpleForm{name='" + name + "'}"; }
}