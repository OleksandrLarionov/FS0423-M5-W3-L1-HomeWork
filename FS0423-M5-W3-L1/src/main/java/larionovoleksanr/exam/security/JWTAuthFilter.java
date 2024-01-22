package larionovoleksanr.exam.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import larionovoleksanr.exam.entities.Dipendente;
import larionovoleksanr.exam.exceptions.UnauthorizedException;
import larionovoleksanr.exam.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private EmployeeService employeeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // doFilterInternal è il metodo di questo filtro che verrà eseguito per ogni richiesta che richieda di essere autenticati, e dovrà:

        // 1. Verifichiamo se la richiesta contiene un Authorization Header ed eventualmente estraiamo il token da esso
        String authHeader = request.getHeader("Authorization"); // "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjOTY0YjQwYy1iMzliLTQ4NzYtYTBkYi1jNDA4Yjc5ZDlhNDUiLCJpYXQiOjE3MDU5MjIyMjYsImV4cCI6MTcwNjUyNzAyNn0.stxc0Bko-lN8ej_Yp8hpjLTQmmlnzqJChrDQ7XkAR0Q"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Per favore metti il token nell'Authorization header");
        } else {
            String accessToken = authHeader.substring(7);

            // 2. Verifichiamo se il token è scaduto o se è stato manipolato (verifica signature)
            jwtTools.verifyToken(accessToken);

            // 3. Se è tutto OK

            // 3.1 Cerco l'utente a DB (l'id sta all'interno del token...)
            String id = jwtTools.extractIdFromToken(accessToken); // L'id è nel token quindi devo estrarlo da lì
            Dipendente employee = employeeService.findById(Long.valueOf(id));

            // 3.2 Informo Spring Security che l'utente è autenticato (se non faccio questo passaggio continuerò ad avere 403 come risposte)
            Authentication authentication = new UsernamePasswordAuthenticationToken(employee, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3.3 Possiamo proseguire al prossimo elemento della chain (e prima o poi si arriverà al controller)
            filterChain.doFilter(request, response); // va al prossimo elemento della catena
            // 4. Se non è OK --> 401
        }
    }

    // Disabilito il filtro per le richieste tipo Login

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Questo metodo serve per specificare quando il filtro NON deve entrare in azione
        // Ad esempio tutte le richieste al controller /auth non devono essere controllate dal filtro

        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
