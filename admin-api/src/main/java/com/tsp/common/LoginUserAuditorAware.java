package com.tsp.common;

import com.tsp.api.domain.user.AuthenticationRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

public class LoginUserAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthenticationRequest) {
            AuthenticationRequest principalDetails = (AuthenticationRequest) principal;
            return ofNullable(principalDetails.getUsername());
        }
        return Optional.empty();
    }
}
