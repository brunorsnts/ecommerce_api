package com.brunosantos.dscatalog.config.security;

import com.brunosantos.dscatalog.config.security.exceptions.InvalidCredentials;
import com.brunosantos.dscatalog.entities.Role;
import com.brunosantos.dscatalog.repositories.UserRepository;
import com.brunosantos.dscatalog.services.exceptions.InvalidLoginException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthenticationService(UserRepository userRepository,
                                 UserDetailsServiceImpl userDetailsService,
                                 BCryptPasswordEncoder passwordEncoder,
                                 JwtEncoder encoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(loginRequest.email());

            if (!userDetails.isLoginCorrect(loginRequest, passwordEncoder)) {
                throw new InvalidCredentials("Username or password is invalid");
            }

            Instant now = Instant.now();
            Long expiresIn = 3600L;

            String scopes = userDetails.get().getRoles().stream()
                    .map(role -> role.name().toLowerCase())
                    .collect(Collectors.joining(" "));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("mybackend")
                    .subject(loginRequest.email())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiresIn))
                    .claim("scope", scopes)
                    .build();

            return new LoginResponse(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), expiresIn);

        } catch (UsernameNotFoundException exception) {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
