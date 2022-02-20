package com.canalbrewing.abcdatacollector.security;

import com.canalbrewing.abcdatacollector.model.UserSession;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    UserSession getUserSession();

    int getUserId();
}
