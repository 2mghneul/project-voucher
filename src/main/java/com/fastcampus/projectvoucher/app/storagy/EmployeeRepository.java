package com.fastcampus.projectvoucher.app.storagy;

import com.fastcampus.projectvoucher.app.storagy.employee.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {}
