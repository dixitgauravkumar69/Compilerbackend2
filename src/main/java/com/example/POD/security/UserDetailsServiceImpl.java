package com.example.POD.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;




import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUserEmail(email);

        System.out.println(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        String rawEmail = (user.getUserEmail() == null) ? "" : user.getUserEmail();
        String rawPassword = (user.getPassword()== null) ? "" : user.getPassword();
        String rawRole = (user.getUserRole() == null || user.getUserRole().trim().isEmpty()) ? "USER" : user.getUserRole();
        
        // Ensure "ROLE_" prefix is added only once
        String roleWithPrefix = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;
        
        // Safety check to avoid Spring Security's "Cannot pass null or empty values to constructor"
        if (rawEmail.isEmpty() || rawPassword.isEmpty()) {
            throw new UsernameNotFoundException("User " + email + " has incomplete credentials (email or password missing in DB)"+user);
        }

        return new org.springframework.security.core.userdetails.User(
                rawEmail,
                rawPassword,
                List.of(new SimpleGrantedAuthority(roleWithPrefix))
        );
    }
}