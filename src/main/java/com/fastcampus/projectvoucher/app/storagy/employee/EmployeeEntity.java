package com.fastcampus.projectvoucher.app.storagy.employee;

import jakarta.persistence.*;

@Table(name = "employee")
@Entity
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    String position;

    @Column
    String department;

    public EmployeeEntity() {
    }

    public EmployeeEntity(String name, String position, String department) {
        this.name = name;
        this.position = position;
        this.department = department;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String position() {
        return position;
    }

    public String department() {
        return department;
    }
}
