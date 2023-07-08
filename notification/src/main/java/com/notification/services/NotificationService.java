package com.notification.services;

import com.common.Employee;
import com.notification.dtos.NotificationRequestDto;
import com.notification.repositries.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmployeeRepository employeeRepository;

    /**
     * Set notification on employees if their department and terminal match based on the given notification request.
     *
     * @param notificationRequestDto the notification request containing the department, terminal, and active status
     */
    public void notify(NotificationRequestDto notificationRequestDto) {

        String department = notificationRequestDto.getDepartment();
        String terminal = notificationRequestDto.getTerminal();

        List<Employee> employeeList = employeeRepository.findByDepartmentAndTerminal(department, terminal);

        if(employeeList.size() != 0) {

            String message = "terminal " + notificationRequestDto.getTerminal() + " at department " + notificationRequestDto.getDepartment();

            if(notificationRequestDto.getIsActive()) {
                message = message + " is now active";
            } else {
                message = message + " is now inactive";
            }

            for(Employee employee: employeeList) {
                employee.setNotification(message);
                employeeRepository.save(employee);
            }
        }

    }

}
