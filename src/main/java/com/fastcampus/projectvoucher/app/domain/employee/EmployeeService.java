package com.fastcampus.projectvoucher.app.domain.employee;

import com.fastcampus.projectvoucher.app.controller.employee.response.EmployeeResponse;
import com.fastcampus.projectvoucher.app.storagy.employee.EmployeeRepository;
import com.fastcampus.projectvoucher.app.storagy.employee.EmployeeEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // 사원 생성
    public Long create(String name, String position, String department) {
        EmployeeEntity employee = employeeRepository.save(new EmployeeEntity(name, position, department));
        return employee.id();
    }

    // 사원 조회
    public EmployeeResponse get(Long no) {
        EmployeeEntity employee = employeeRepository.findById(no)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사원입니다."));
        return EmployeeResponse.fromEntity(employee);
    }

}
