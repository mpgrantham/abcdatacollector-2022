package com.canalbrewing.abcdatacollector.security;

import com.canalbrewing.abcdatacollector.model.UserSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserSession getUserSession() {
        Authentication authentication = getAuthentication();
        return (UserSession) authentication.getPrincipal();
    }

    @Override
    public int getUserId() {
        return getUserSession().getUserId();
    }

}
