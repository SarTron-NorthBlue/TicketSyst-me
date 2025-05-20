package com.example.ticketapp.service;

import com.example.ticketapp.model.User;
import com.example.ticketapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public com.example.ticketapp.model.User loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Tentative de connexion pour: " + username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Utilisateur non trouvé: " + username);
                    return new UsernameNotFoundException("Utilisateur non trouvé");
                });
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
