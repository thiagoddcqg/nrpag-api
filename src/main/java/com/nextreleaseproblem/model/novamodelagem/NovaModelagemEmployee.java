package com.nextreleaseproblem.model.novamodelagem;

import java.util.List;

public class NovaModelagemEmployee {
    public void setId(int id) {
        this.id = id;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    private int id;
    private double capacity;
    private List<String> skills;

    // Construtor, getters e setters

    public NovaModelagemEmployee(int id, double capacity, List<String> skills) {
        this.id = id;
        this.capacity = capacity;
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public double getCapacity() {
        return capacity;
    }

    public List<String> getSkills() {
        return skills;
    }
}
