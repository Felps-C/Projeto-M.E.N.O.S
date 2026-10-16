package com.example.M.E.N.O.S.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

// Único lugar que mexe com senha: gera o hash (BCrypt) e confere no login.
@Component
public class SenhaService {

    private static final int MAXIMO_BYTES = 72; // limite do BCrypt

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public String gerarHash(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha é obrigatória");
        }
        if (senha.getBytes(StandardCharsets.UTF_8).length > MAXIMO_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha é grande demais (máximo de 72 bytes)");
        }
        return encoder.encode(senha);
    }

    // Confere a senha digitada com a guardada.
    // Contas antigas (criadas antes do hash) têm a senha em texto puro: ainda entram,
    // e o AuthService troca por hash logo em seguida (veja precisaAtualizar).
    public boolean confere(String senhaDigitada, String senhaGuardada) {
        if (senhaDigitada == null || senhaGuardada == null) {
            return false;
        }
        if (ehHash(senhaGuardada)) {
            return encoder.matches(senhaDigitada, senhaGuardada);
        }
        return MessageDigest.isEqual(
                senhaDigitada.getBytes(StandardCharsets.UTF_8),
                senhaGuardada.getBytes(StandardCharsets.UTF_8));
    }

    // true = a senha guardada ainda é texto puro e deve ser trocada por hash.
    public boolean precisaAtualizar(String senhaGuardada) {
        return senhaGuardada != null && !ehHash(senhaGuardada);
    }

    // Hash BCrypt: começa com $2a$, $2b$ ou $2y$ e tem 60 caracteres.
    private boolean ehHash(String valor) {
        return valor.length() == 60 && valor.startsWith("$2") && valor.charAt(3) == '$';
    }
}
