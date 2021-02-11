package com.engagetech.codechallenge.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * Class for providing current user information
 */
@Slf4j
public class SecurityAuditorAware implements AuditorAware<String> {

    /**
     * Provides current user id (if exists)
     * @return
     */
    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails ud = (UserDetails) principal;
            return Optional.of(ud.getUsername());
        }
        return Optional.of(principal.toString());
    }
}