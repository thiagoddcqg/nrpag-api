package com.nextreleaseproblem.service.novamodelagem;

import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFeature;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemNRPModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NovaModelagemGeneticAlgorithm {
    private NovaModelagemNRPModel model;
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;
    private int generations;

    public NovaModelagemGeneticAlgorithm(NovaModelagemNRPModel model, int populationSize, double crossoverRate, double mutationRate, int generations) {
        this.model = model;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.generations = generations;
    }

    public List<NovaModelagemFeature> run() {
        List<List<NovaModelagemFeature>> population = initializePopulation();
        for (int gen = 0; gen < generations; gen++) {
            population = evolve(population);
        }
        return getBestSolution(population);
    }

    private List<List<NovaModelagemFeature>> initializePopulation() {
        List<List<NovaModelagemFeature>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(randomSolution());
        }
        return population;
    }

    private List<NovaModelagemFeature> randomSolution() {
        List<NovaModelagemFeature> solution = new ArrayList<>();
        Random rand = new Random();
        for (NovaModelagemFeature feature : model.getFeatures()) {
            if (rand.nextBoolean()) {
                solution.add(feature);
            }
        }
        return solution;
    }

    private List<List<NovaModelagemFeature>> evolve(List<List<NovaModelagemFeature>> population) {
        List<List<NovaModelagemFeature>> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<NovaModelagemFeature> parent1 = selectParent(population);
            List<NovaModelagemFeature> parent2 = selectParent(population);
            List<NovaModelagemFeature> child = crossover(parent1, parent2);
            mutate(child);
            newPopulation.add(child);
        }
        return newPopulation;
    }

    private List<NovaModelagemFeature> selectParent(List<List<NovaModelagemFeature>> population) {
        Random rand = new Random();
        return population.get(rand.nextInt(populationSize));
    }

    private List<NovaModelagemFeature> crossover(List<NovaModelagemFeature> parent1, List<NovaModelagemFeature> parent2) {
        List<NovaModelagemFeature> child = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < parent1.size(); i++) {
            if (rand.nextDouble() < crossoverRate) {
                child.add(parent1.get(i));
            } else if(parent2.size() > i) {
                child.add(parent2.get(i));
            } else {
                child.add(parent1.get(i));
            }
        }
        return child;
    }

    private void mutate(List<NovaModelagemFeature> solution) {
        Random rand = new Random();
        for (int i = 0; i < solution.size(); i++) {
            if (rand.nextDouble() < mutationRate) {
                solution.set(i, model.getFeatures().get(rand.nextInt(model.getFeatures().size())));
            }
        }
    }

    private List<NovaModelagemFeature> getBestSolution(List<List<NovaModelagemFeature>> population) {
        List<NovaModelagemFeature> bestSolution = null;
        double bestValue = -Double.MAX_VALUE;
        for (List<NovaModelagemFeature> solution : population) {
            double value = calculateValue(solution);
            if (value > bestValue) {
                bestValue = value;
                bestSolution = solution;
            }
        }
        return bestSolution;
    }

    private double calculateValue(List<NovaModelagemFeature> solution) {
        double value = 0;
        double effort = 0;
        for (NovaModelagemFeature feature : solution) {
            value += feature.getBusinessValue();
            effort += feature.getEffort();
        }
        if (effort > model.getMaxEffort() || solution.size() > model.getMaxFeatures()) {
            return -Double.MAX_VALUE;
        }
        return value;
    }
}
