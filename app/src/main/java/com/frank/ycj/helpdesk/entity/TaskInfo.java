package com.frank.ycj.helpdesk.entity;

import cn.bmob.v3.BmobObject;

public class TaskInfo extends BmobObject {
    private String taskId;
    private String taskName;
    private String taskContent;
    private String taskPublishDate;
    private String taskExpectDate;
    private String taskExecuteDate;
    private String taskSolvedDate;
    private String taskSolvedMan;
    private String taskSolvedMethod;
    private String importantTask;
    private String importantPosition;
    private String importantDegree;
    private String taskState;
    private String taskStyle;
    private String publisher;
    private String publisherName;
    private String installDate;
    private String installState;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskPublishDate() {
        return taskPublishDate;
    }

    public void setTaskPublishDate(String taskPublishDate) {
        this.taskPublishDate = taskPublishDate;
    }

    public String getTaskExpectDate() {
        return taskExpectDate;
    }

    public void setTaskExpectDate(String taskExpectDate) {
        this.taskExpectDate = taskExpectDate;
    }

    public String getTaskExecuteDate() {
        return taskExecuteDate;
    }

    public void setTaskExecuteDate(String taskExecuteDate) {
        this.taskExecuteDate = taskExecuteDate;
    }

    public String getTaskSolvedDate() {
        return taskSolvedDate;
    }

    public void setTaskSolvedDate(String taskSolvedDate) {
        this.taskSolvedDate = taskSolvedDate;
    }

    public String getImportantTask() {
        return importantTask;
    }

    public void setImportantTask(String importantTask) {
        this.importantTask = importantTask;
    }

    public String getImportantPosition() {
        return importantPosition;
    }

    public void setImportantPosition(String importantPosition) {
        this.importantPosition = importantPosition;
    }

    public String getImportantDegree() {
        return importantDegree;
    }

    public void setImportantDegree(String importantDegree) {
        this.importantDegree = importantDegree;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTaskSolvedMan() {
        return taskSolvedMan;
    }

    public void setTaskSolvedMan(String taskSolvedMan) {
        this.taskSolvedMan = taskSolvedMan;
    }

    public String getTaskSolvedMethod() {
        return taskSolvedMethod;
    }

    public void setTaskSolvedMethod(String taskSolvedMethod) {
        this.taskSolvedMethod = taskSolvedMethod;
    }

    public String getTaskStyle() {
        return taskStyle;
    }

    public void setTaskStyle(String taskStyle) {
        this.taskStyle = taskStyle;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public String getInstallState() {
        return installState;
    }

    public void setInstallState(String installState) {
        this.installState = installState;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
