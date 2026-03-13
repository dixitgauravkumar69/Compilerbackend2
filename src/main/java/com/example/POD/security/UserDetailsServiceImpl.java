package com.example.POD.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Database se user dhoondh rha hai
        UserEntity user = userRepository.findByUserEmail(email);

        // 2. Spring Security ke compatible User object mein convert karna
        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}