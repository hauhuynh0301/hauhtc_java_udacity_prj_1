package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.SuperUser;
import com.udacity.jwdnd.course1.cloudstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SuperUser superUser = userRepository.findByUsername(username);
        if (Objects.isNull(superUser)) {
            throw new UsernameNotFoundException("User not found");
        }
        return superUser;
    }

    public SuperUser register(SuperUser superUser) {
        String encodedPassword = passwordEncoder.encode(superUser.getPassword());
        superUser.setPassword(encodedPassword);
        superUser.setEnabled(true);
        superUser.setRole("USER");
        try {
            userRepository.insertUser(superUser);
        } catch (Exception e) {
            throw e;
        }
        return superUser;
    }
}
