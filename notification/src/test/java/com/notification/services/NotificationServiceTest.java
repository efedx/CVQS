package com.notification.services;

import com.notification.dtos.NotificationRequestDto;
import com.notification.entities.Employee;
import com.notification.services.NotificationService;
import com.notification.repositries.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void testNotify() {

        // given
        String department1 = "INSPECTION";
        String terminal1 = "RIO";
        NotificationRequestDto requestDtoActive = new NotificationRequestDto(department1, terminal1, true);

        String department2 = "ANL";
        String terminal2 = "CNN";
        NotificationRequestDto requestDtoInactive = new NotificationRequestDto(department2, terminal2, false);

        Employee employee1 = Employee.builder()
                .username("user1")
                .terminal(terminal1)
                .terminal(terminal1)
                .build();

        Employee employee2 = Employee.builder()
                .username("user2")
                .terminal(terminal2)
                .terminal(terminal2)
                .build();

        List<Employee> employeeList = Arrays.asList(employee1, employee2);

        // when
        when(employeeRepository.findByDepartmentAndTerminal(department1, terminal1)).thenReturn(List.of(employee1));
        when(employeeRepository.findByDepartmentAndTerminal(department2, terminal2)).thenReturn(List.of(employee2));

        // then
        notificationService.notify(requestDtoActive);
        notificationService.notify(requestDtoInactive);

        // Assert
        Mockito.verify(employeeRepository, times(2)).save(any(Employee.class));
        assertEquals("terminal RIO at department INSPECTION is now active", employee1.getNotification());
        assertEquals("terminal CNN at department ANL is now inactive", employee2.getNotification());
    }
}