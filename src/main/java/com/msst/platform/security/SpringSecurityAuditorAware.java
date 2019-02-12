package com.msst.platform.security;

import com.msst.platform.config.Constants;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // There is currently no reactive AuditorAware implementation so we can't
        // extract the currently logged-in user from the Reactor Context.
        // Therefore createdBy and lastModifiedBy will have to be set explicitly.
        // See https://jira.spring.io/browse/DATACMNS-1231
        return Optional.of(Constants.SYSTEM_ACCOUNT);
    }
}
