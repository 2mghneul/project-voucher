package com.fastcampus.projectvoucher.app.response;

import com.fastcampus.projectvoucher.app.storagy.employee.EmployeeEntity;

public record EmployeeResponse(Long no, String name, String position, String department) {
    public static EmployeeResponse fromEntity(EmployeeEntity entity) {
        return new EmployeeResponse(
                entity.id(),
                entity.name(),
                entity.position(),
                entity.department()
        );
    }
}
