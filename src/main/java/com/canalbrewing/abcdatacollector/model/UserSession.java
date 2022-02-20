package com.canalbrewing.abcdatacollector.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserSession implements UserDetails {

    private String sessionToken;

    private int userId;

    private Date sessionDate;

    private int sessionActiveFl;

    private Collection<? extends GrantedAuthority> authorities;

    private String email;

    private String displayName;

    private String password;

    private String username;

    private Boolean enabled = true;

    private Boolean accountNonExpired = true;

    private Boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public int getSessionActiveFl() {
        return sessionActiveFl;
    }

    public void setSessionActiveFl(int sessionActiveFl) {
        this.sessionActiveFl = sessionActiveFl;
    }

    @Override
    public String toString() {
        return "UserSession [sessionActiveFl=" + sessionActiveFl + ", sessionDate=" + sessionDate + ", sessionToken="
                + sessionToken + ", userId=" + userId + "]";
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void eraseCredentials() {
        this.password = null;
    }

}