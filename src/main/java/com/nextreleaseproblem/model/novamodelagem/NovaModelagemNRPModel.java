package com.nextreleaseproblem.model.novamodelagem;

import java.util.List;

public class NovaModelagemNRPModel {
    public void setFeatures(List<NovaModelagemFeature> features) {
        this.features = features;
    }

    public void setEmployees(List<NovaModelagemEmployee> employees) {
        this.employees = employees;
    }

    public void setMaxFeatures(double maxFeatures) {
        this.maxFeatures = maxFeatures;
    }

    public void setMaxEffort(double maxEffort) {
        this.maxEffort = maxEffort;
    }

    public List<NovaModelagemFeature> getFeatures() {
        return features;
    }

    public List<NovaModelagemEmployee> getEmployees() {
        return employees;
    }

    public double getMaxFeatures() {
        return maxFeatures;
    }

    public double getMaxEffort() {
        return maxEffort;
    }

    private List<NovaModelagemFeature> features;
    private List<NovaModelagemEmployee> employees;
    private double maxFeatures;
    private double maxEffort;

    public NovaModelagemNRPModel(List<NovaModelagemFeature> features, List<NovaModelagemEmployee> employees, double maxFeatures, double maxEffort) {
        this.features = features;
        this.employees = employees;
        this.maxFeatures = maxFeatures;
        this.maxEffort = maxEffort;
    }
}