package com.merge_conflict.AuthModule.Auth;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    // Mapa para rastrear los intentos fallidos de inicio de sesión.
    // La clave es el nombre de usuario y el valor es un objeto que contiene el número de intentos y el tiempo de bloqueo.
    private final Map<String, LoginAttempt> loginAttempts = new HashMap<>();

    // Número máximo de intentos fallidos antes de bloquear al usuario.
    private static final int MAX_ATTEMPTS = 3;
    // Duración del bloqueo en minutos.
    private static final int LOCK_TIME = 5;

    public void addLoginAttempt(String username) {
        // Obtiene el objeto LoginAttempt para el usuario, o crea uno nuevo si no existe.
        LoginAttempt attempt = loginAttempts.getOrDefault(username, new LoginAttempt());
        attempt.incrementAttempts();

        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            attempt.setBlocked(true);
            attempt.setBlockedUntil(LocalDateTime.now().plusMinutes(LOCK_TIME));
        }
        loginAttempts.put(username, attempt);
    }

    public boolean isBlocked(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null || !attempt.isBlocked()) {
            return false;
        }
        if (LocalDateTime.now().isAfter(attempt.getBlockedUntil())) {
            // El tiempo de bloqueo ha expirado, desbloquear al usuario.
            attempt.setBlocked(false);
            attempt.setAttempts(0); // Opcionalmente reinicia el contador de intentos fallidos.
            return false;
        }
        return true;
    }

    public void resetLoginAttempts(String username) {
        loginAttempts.remove(username);
    }

    // Clase interna para manejar los intentos de inicio de sesión.
    private static class LoginAttempt {
        private int attempts;
        private boolean isBlocked;
        private LocalDateTime blockedUntil;

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attemps) {
            this.attempts = attemps;
        }

        public void incrementAttempts() {
            this.attempts++;
        }

        public boolean isBlocked() {
            return isBlocked;
        }

        public void setBlocked(boolean blocked) {
            this.isBlocked = blocked;
        }

        public LocalDateTime getBlockedUntil() {
            return blockedUntil;
        }

        public void setBlockedUntil(LocalDateTime blockedUntil) {
            this.blockedUntil = blockedUntil;
        }
    }
}
