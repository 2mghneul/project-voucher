package com.fastcampus.projectvoucher.app.controller;

import com.fastcampus.projectvoucher.app.domain.employee.EmployeeService;
import com.fastcampus.projectvoucher.app.request.EmployeeCreateRequest;
import com.fastcampus.projectvoucher.app.response.EmployeeResponse;
import org.springframework.web.bind.annotation.*;


@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 사원 생성
    @PostMapping("api/v1/employee")
    public Long create(@RequestBody final EmployeeCreateRequest request) {
        return employeeService.create(request.name(), request.position(), request.department());
    }

    // 사원 조회
    @GetMapping("api/v1/employee/{no}")
    public EmployeeResponse get(@PathVariable final Long no) {
        return employeeService.get(no);
    }
}
