package com.notification;

import com.common.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.department = ?1 AND e.terminal = ?2")
    List<Employee> findByDepartmentAndTerminal(String department, String terminal);

}
