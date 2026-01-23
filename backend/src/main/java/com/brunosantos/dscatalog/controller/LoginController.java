package com.brunosantos.dscatalog.controller;

import com.brunosantos.dscatalog.config.security.UserDetailsImpl;
import com.brunosantos.dscatalog.config.security.UserDetailsServiceImpl;
import com.brunosantos.dscatalog.dto.LoginRequest;
import com.brunosantos.dscatalog.dto.LoginResponse;
import com.brunosantos.dscatalog.entities.Role;
import com.brunosantos.dscatalog.services.UserService;
import com.brunosantos.dscatalog.services.exceptions.InvalidLoginException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginController(UserDetailsServiceImpl userDetailsService,
                           JwtEncoder jwtEncoder,
                           UserService userService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
            if (!userDetails.isLoginCorrect(loginRequest, passwordEncoder)) {
                throw new UsernameNotFoundException("Username or password is invalid");
            }

            var now = Instant.now();
            var expiresIn = 300L;

            var scopes = userDetails.get().getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(" "));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("mybackend")
                    .subject(loginRequest.email())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiresIn))
                    .claim("scope", scopes)
                    .build();

            String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));

        } catch (UsernameNotFoundException exception) {
            throw new InvalidLoginException("Username or password is invalid");
        }
    }
}
