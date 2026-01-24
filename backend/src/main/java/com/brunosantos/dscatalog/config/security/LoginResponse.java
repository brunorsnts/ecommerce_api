package com.brunosantos.dscatalog.config.security;

public record LoginResponse(String accessToken, Long expiresIn) {
}
