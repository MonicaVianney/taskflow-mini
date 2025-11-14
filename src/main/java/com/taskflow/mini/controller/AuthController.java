package com.taskflow.mini.controller;

import com.taskflow.mini.model.User;
import com.taskflow.mini.repository.UserRepository;
import com.taskflow.mini.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<?> register(@RequestBody User user) {
        // Verificar si el email ya existe
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El email ya está registrado");
        }

        // Encriptar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar el usuario
        User savedUser = userRepository.save(user);

        // No devolver la contraseña en la respuesta
        savedUser.setPassword(null);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        // Buscar usuario por email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales invalidas");
        }

        User user = userOptional.get();

        // Verificar contraseña
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales invalidas");
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(user.getEmail());

        // Crear respuesta con el token
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }
}
