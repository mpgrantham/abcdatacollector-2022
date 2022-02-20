package com.canalbrewing.abcdatacollector.security;

import com.canalbrewing.abcdatacollector.business.UserBusiness;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserBusiness userBusiness;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        //
    }

    @Override
    protected UserDetails retrieveUser(String userName,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        UserSession userSession = null;
        Object token = usernamePasswordAuthenticationToken.getCredentials();

        try {
            userSession = userBusiness.getUserSession((String) token);

        } catch (AbcDataCollectorException ex) {
            throw new UsernameNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new UsernameNotFoundException(ex.getMessage());
        }

        return userSession;

    }

}
