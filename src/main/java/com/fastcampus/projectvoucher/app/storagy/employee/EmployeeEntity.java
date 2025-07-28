package com.fastcampus.projectvoucher.app.storagy.employee;

import com.fastcampus.projectvoucher.app.storagy.BaseEntity;
import jakarta.persistence.*;

@Table(name = "employee")
@Entity
public class EmployeeEntity extends BaseEntity {
    @Column
    private String name;

    @Column
    private String position;

    @Column
    private String department;

    public EmployeeEntity() {
    }

    public EmployeeEntity(String name, String position, String department) {
        this.name = name;
        this.position = position;
        this.department = department;
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
