package com.projeto.matriculafacil.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service 
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    // Converte a string secreta na chave de criptografia que o JWT exige
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Método que gera token
    public String generateToken(String alunoID) {
        return Jwts.builder()
                .subject(alunoID) // Coloca o UUID do aluno dentro do token
                .issuedAt(new Date(System.currentTimeMillis())) // Data e hora atual
                .expiration(new Date(System.currentTimeMillis() + expirationTime)) // Data e hora de vencimento
                .signWith(getSigningKey()) // Assina digitalmente com a chave secreta
                .compact(); // Finaliza e transforma tudo numa String
    }

    // Método para validar token
    public String validateToken(String token) {
        token = token.replace("Bearer ", "");
        try{
            return Jwts.parser()
                    .verifyWith(getSigningKey()) // Confere se a assinatura é sua
                    .build()
                    .parseSignedClaims(token) // Descriptografa
                    .getPayload()
                    .getSubject(); // Pega o conteúdo
        } catch (Exception e){
            return "";
        }
    }
}