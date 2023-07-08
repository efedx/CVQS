package com.notification;

import com.common.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmployeeRepository employeeRepository;

    public void notify(NotificationRequestDto notificationRequestDto) {

        String department = notificationRequestDto.getDepartment();
        String terminal = notificationRequestDto.getTerminal();

        List<Employee> employeeList = employeeRepository.findByDepartmentAndTerminal(department, terminal);

        String message;

        if(notificationRequestDto.getIsActive()) {
            message =
                    "terminal " + notificationRequestDto.getTerminal()
                    + " at department " + notificationRequestDto.getDepartment()
                    + " is now active";
        } else {
            message =
                    "terminal " + notificationRequestDto.getTerminal()
                            + " at department " + notificationRequestDto.getDepartment()
                            + " is now inactive";
        }

        for(Employee employee: employeeList) {
            employee.setNotification(message);
            employeeRepository.save(employee);
        }

    }

}
