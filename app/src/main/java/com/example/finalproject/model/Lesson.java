package com.example.finalproject.model;

import java.util.HashMap;

public class Lesson {
    private String date;
    private String startTime;
    private String endTime;
    private String studentId;
    private String teacherId;
    private String subject;
    private String lessonId;

    public String getDate() {
        return date;
    }

    public Lesson setDate(String date) {
        this.date = date;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public Lesson setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getStudentId() {
        return studentId;
    }

    public Lesson setStudentId(String studentId) {
        this.studentId = studentId;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public Lesson setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public Lesson setTeacherId(String teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Lesson setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Lesson(String date, String startTime, String endTime, String studentId, String teacherId, String subject) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.subject = subject;
    }
    public Lesson(String date, String startTime, String endTime, String studentId, String teacherId, String subject, String lessonId) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.subject = subject;
        this.lessonId = lessonId;
    }


    public String getLessonId() {
        return lessonId;
    }

    public Lesson setLessonId(String lessonId) {
        this.lessonId = lessonId;
        return this;
    }

    public Object toHashMap() {
        HashMap<String,Object> otherResult= new HashMap<>();
        otherResult.put("date",this.date);
        otherResult.put("start",this.startTime);
        otherResult.put("end",this.endTime);
        otherResult.put("student",this.studentId);
        otherResult.put("teacher",this.teacherId);
        otherResult.put("subject", this.subject);
        return otherResult;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", studentId='" + studentId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", subject='" + subject + '\'' +
                ", lessonId='" + lessonId + '\'' +
                '}';
    }
}
