package com.nextreleaseproblem.model.novamodelagem;

import java.time.LocalDate;
import java.util.List;

public class NovaModelagemFeature {
    public void setId(int id) {
        this.id = id;
    }

    public void setBusinessValue(double businessValue) {
        this.businessValue = businessValue;
    }

    public void setEffort(double effort) {
        this.effort = effort;
    }

    public void setPrecedence(List<Integer> precedence) {
        this.precedence = precedence;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setServiceQuantity(int serviceQuantity) {
        this.serviceQuantity = serviceQuantity;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setParentTask(int parentTask) {
        this.parentTask = parentTask;
    }

    public void setAttachmentQuantity(int attachmentQuantity) {
        this.attachmentQuantity = attachmentQuantity;
    }

    public int getId() {
        return id;
    }

    public double getBusinessValue() {
        return businessValue;
    }

    public double getEffort() {
        return effort;
    }

    public List<Integer> getPrecedence() {
        return precedence;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public int getServiceQuantity() {
        return serviceQuantity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getParentTask() {
        return parentTask;
    }

    public int getAttachmentQuantity() {
        return attachmentQuantity;
    }

    private int id;
    private double businessValue;
    private double effort;
    private List<Integer> precedence;
    private String type;
    private String status;
    private String title;
    private String assignedTo;
    private int serviceQuantity;
    private LocalDate startDate;
    private int parentTask;
    private int attachmentQuantity;

    // Construtor, getters e setters

    public NovaModelagemFeature(int id, double businessValue, double effort, List<Integer> precedence, String type, String status, String title, String assignedTo, int serviceQuantity, LocalDate startDate, int parentTask, int attachmentQuantity) {
        this.id = id;
        this.businessValue = businessValue;
        this.effort = effort;
        this.precedence = precedence;
        this.type = type;
        this.status = status;
        this.title = title;
        this.assignedTo = assignedTo;
        this.serviceQuantity = serviceQuantity;
        this.startDate = startDate;
        this.parentTask = parentTask;
        this.attachmentQuantity = attachmentQuantity;
    }
}