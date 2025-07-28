package com.fastcampus.projectvoucher.app.response;

import com.fastcampus.projectvoucher.app.storagy.employee.EmployeeEntity;

import java.time.LocalDateTime;

public record EmployeeResponse(Long no, String name, String position, String department, LocalDateTime createTime, LocalDateTime updateTime) {
    public static EmployeeResponse fromEntity(EmployeeEntity entity) {
        return new EmployeeResponse(
                entity.id(),
                entity.name(),
                entity.position(),
                entity.department(),
                entity.createdAt(),
                entity.updateAt()
        );
    }
}
