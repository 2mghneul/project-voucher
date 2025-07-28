package com.fastcampus.projectvoucher.app.controller;

import com.fastcampus.projectvoucher.app.domain.employee.EmployeeService;
import com.fastcampus.projectvoucher.app.response.EmployeeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class EmployeeControllerTest {
    @Autowired
    private EmployeeService employeeService;

    @DisplayName("회원 생성 후 조회가 가능하다.")
    @Test
    void test1() {
        // given
        String name = "deft";
        String position = "ad";
        String department = "kt";

        // when
        Long no = employeeService.create(name, position, department);
        EmployeeResponse response = employeeService.get(no);

        // then
        assertThat(response).isNotNull();
        assertThat(response.no()).isEqualTo(no);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.position()).isEqualTo(position);
        assertThat(response.department()).isEqualTo(department);
    }
}
