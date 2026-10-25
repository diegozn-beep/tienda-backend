package com.diego.tiendabackend.controller;

import com.diego.tiendabackend.dto.AuthResponse;
import com.diego.tiendabackend.dto.LoginRequest;
import com.diego.tiendabackend.dto.RegisterRequest;
import com.diego.tiendabackend.model.User;
import com.diego.tiendabackend.repository.UserRepository;
import com.diego.tiendabackend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        if (request.name == null || request.email == null || request.password == null
                || request.name.isBlank() || request.email.isBlank() || request.password.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completa todos los campos. La contraseña debe tener al menos 6 caracteres.");
        }

        if (userRepository.findByEmail(request.email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ese correo ya está registrado.");
        }

        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRole("CLIENT");
        userRepository.save(user);

        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getName(), user.getRole());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email).orElse(null);

        if (user == null || !passwordEncoder.matches(request.password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
        }

        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getName(), user.getRole());
    }
}