package com.taskflow.mini.security;

import com.taskflow.mini.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Garantiza que el filtro se ejecute una sola vez por peticion y evita ejecuciones duplicadas

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Obtener el header Authorization
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // Verificar si el header tiene el formato correcto
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remover "Bearer " del inicio
            email = jwtUtil.extractEmail(token);
        }

        // Validar el token
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token, email)) {

                // Crear objeto de autenticacion y agregarla al contexto de seguridad
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); // aqui se guarda el objeto de autenticacion
            }
        }

        // Continuar con la cadena de filtros (deja pasar la peticion al endpoint)
        filterChain.doFilter(request, response);
    }
}
