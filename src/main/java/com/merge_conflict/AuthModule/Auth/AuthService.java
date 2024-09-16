package com.merge_conflict.AuthModule.Auth;

import com.merge_conflict.AuthModule.AuthJwt.JwtService;
import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalEmailArgumentException;
import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalLoginException;
import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalNullArgumentException;
import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalPasswordArgumentException;
import com.merge_conflict.AuthModule.User.Role;
import com.merge_conflict.AuthModule.User.User;
import com.merge_conflict.AuthModule.User.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail();

        //Validate that elements aren't empty
        if (email == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalNullArgumentException("Email cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalNullArgumentException("Password cannot be empty");
        }

        // Verify if the user isn't blocked
        if (loginAttemptService.isBlocked(email)) {
            throw new IllegalLoginException("User temporarily blocked. Please try again later");
        }

        try {
            // Attempt to authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));

            // Retrieve user details
            UserDetails user = userRepository.findByEmail(email).orElseThrow();

            // Generate a JWT token
            String token = jwtService.getToken(user);

            // Reset failed attempts if authentication is successful
            loginAttemptService.resetLoginAttempts(email);

            return AuthResponse.builder()
                    .token(token)
                    .build();
        } catch (Exception e) {
            // Log the failed attempt
            loginAttemptService.addLoginAttempt(email);
            throw new IllegalLoginException("Incorrect credentials");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalNullArgumentException("Email cannot be empty");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalEmailArgumentException("Email is already in use");
        }
        if (!request.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
            throw new IllegalEmailArgumentException("Invalid email format");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalNullArgumentException("Password cannot be empty");
        }

        // Verifies that the password has at least 10 characters, contains letters, and numbers
        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$")){
            throw new IllegalPasswordArgumentException("password has at least 10 characters, contains letters, and numbers");
        }
        if (request.getAge() <16 || request.getAge() > 120) {
            throw new IllegalNullArgumentException("Age must be between 16 and 120");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode( request.getPassword()))
                .age(request.age)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}