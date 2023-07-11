package com.security.config;

import com.security.entities.Employee;
import com.security.entities.Roles;
import com.security.exceptions.CustomUsernameNotFoundException;
import com.security.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws CustomUsernameNotFoundException {
        Employee employee = employeeRepository
                .findByUsername(username)
                .orElseThrow(() -> new CustomUsernameNotFoundException("User not found with username : " + username));

        Set<Roles> roles = employee.getRoles();
        Set<GrantedAuthority> grantedAuthorities = getSimpleGrantedAuthoritiesFromRolesSet(roles);

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

    public Set<GrantedAuthority> getSimpleGrantedAuthoritiesFromRolesSet(Set<Roles> rolesSet) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for(Roles role: rolesSet) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return grantedAuthorities;
    }
}
