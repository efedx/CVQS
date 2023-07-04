package com.employee.services;

import com.employee.repository.EmployeeRepository;
import com.employee.model.Employee;
import com.employee.model.Roles;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    //-----------------------------------------------------------------------------------------------

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        Set<Roles> roles = employee.getRoles();
        Set<GrantedAuthority> grantedAuthorities = getSimpleGrantedAuthoritiesSetFromRolesSet(roles);

        return org.springframework.security.core.userdetails.User
                .withUsername(employee.getUsername())
                .password(employee.getPassword())
                .authorities(grantedAuthorities)
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .credentialsExpired(false)
                .build();
    }

    //-----------------------------------------------------------------------------------------------

    public Set<GrantedAuthority> getSimpleGrantedAuthoritiesSetFromRolesSet(Set<Roles> rolesSet) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for(Roles role: rolesSet) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return grantedAuthorities;
    }
}
