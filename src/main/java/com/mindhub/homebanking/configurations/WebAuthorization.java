package com.mindhub.homebanking.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/index.html","/web/css/**","/web/js/**","/web/img/**","/web/access-denied.html").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers(HttpMethod.POST,"/api/loans","/api/clients/current/accounts","/api/clients/current/cards","/api/accounts/filterAccounts").hasAuthority("CLIENT")
                .antMatchers("/web/**","/api/loans","/api/transactions","/api/accounts/**","/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers("/api/clients/**","/manager.html","/manager.js","/rest/**","/h2-console/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/api/changeAuthority").hasAuthority("ADMIN")
                .anyRequest().denyAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        // Para acceder a cualquier URL que comience con "/admin", el usuario debe tener el rol ADMIN
        // Para acceder a cualquier otra URL, solo necesita iniciar sesión con un rol de USER
        // Se crea el rol "ADMIN" para ingresar a las rutas de REST o a la consola H2.

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        http.exceptionHandling().accessDeniedHandler((req, res, exc) -> {
            res.sendRedirect("/web/access-denied.html");
        });

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> {
            if(req.getRequestURI().equals("/error")){
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else {
                res.sendRedirect("/web/index.html");
            }
        });

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> {
            res.setHeader("userRole",auth.getAuthorities().toString());
            clearAuthenticationAttributes(req);
        });

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        });

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler((req, res, auth) -> {
            res.sendRedirect("/web/index.html");
        });

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
