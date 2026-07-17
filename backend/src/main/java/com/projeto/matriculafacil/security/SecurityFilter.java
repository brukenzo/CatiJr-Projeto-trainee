package com.projeto.matriculafacil.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projeto.matriculafacil.aluno.IAlunoRepository;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
    @Autowired
    JwtService jwtService;

    @Autowired
    IAlunoRepository alunoRepository;

    @Override // Faz a validação do token
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");

        if (header != null){
            var conteudoToken = jwtService.validateToken(header);

            if (!conteudoToken.isEmpty()){
                var alunoId = UUID.fromString(conteudoToken);
                var alunoOptional = alunoRepository.findById(alunoId);
                
                if (alunoOptional.isPresent()){
                    var aluno = alunoOptional.get();

                    var auth = new UsernamePasswordAuthenticationToken(aluno, null, null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
