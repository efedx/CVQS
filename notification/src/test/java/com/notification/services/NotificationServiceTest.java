package com.notification.services;

import com.common.Employee;
import com.notification.dtos.NotificationRequestDto;
import com.notification.repositries.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private NotificationService notificationService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        notificationService = new NotificationService(employeeRepository);
//    }

    @Test
    void notify_ActiveTerminal_NotifiesEmployees() {

        // given
        String department = "IT";
        String terminal = "A1";
        NotificationRequestDto requestDto = new NotificationRequestDto(department, terminal, true);

        Employee employee1 = Employee.builder()
                .username("user1")
                .terminal(terminal)
                .terminal(terminal)
                .build();

        Employee employee2 = Employee.builder()
                .username("user2")
                .terminal(terminal)
                .terminal(terminal)
                .build();

        List<Employee> employeeList = Arrays.asList(employee1, employee2);

        // when
        when(employeeRepository.findByDepartmentAndTerminal(department, terminal)).thenReturn(employeeList);

        // then
        notificationService.notify(requestDto);

        // Assert
        Mockito.verify(employeeRepository, times(2)).save(any(Employee.class));
        assertEquals("terminal A1 at department IT is now active", employee1.getNotification());
        assertEquals("terminal A1 at department IT is now active", employee2.getNotification());
    }
}