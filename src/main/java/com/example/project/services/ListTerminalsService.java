package com.example.project.services;

import com.example.project.model.Department;
import com.example.project.model.Terminal;
import com.example.project.repository.DepartmentRepository;
import com.example.project.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListTerminalsService {

    @Autowired
    TerminalRepository terminalRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    public Page<Terminal> getActiveTerminalsPage(int pageNumber, String sortDirection, String terminalName) {

        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by("id").ascending()
                : Sort.by("id").descending());

        Page<Terminal> activeTerminalsPage = terminalRepository.findByActiveTerminalsAndTerminalName(terminalName, pageable);

        return activeTerminalsPage;
    }

    public Page<Terminal> getActiveTerminalsPage(int pageNumber, String sortDirection) {

        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by("departmentName").ascending()
                : Sort.by("departmentName").descending());
        Page<Terminal> activeTerminalsPage = terminalRepository.findAllByActiveTerminals(pageable);
        return activeTerminalsPage;
    }
}
