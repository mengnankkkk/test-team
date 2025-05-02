package com.mengnnakk.entry.sign;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "class_student")
public class ClassStudent {
    @Id
    @GeneratedValue
    private Long id;
    private String classId;
    private String studentId;
    // getters/setters
}