package com.springboot.app.security.user_principle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private Long id;
    @JsonIgnore
    private String password;
    private String username;
    private int wallet;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> roles;

    //Generate user for security
    public static UserPrincipal build(User user) {
        List<GrantedAuthority> authorities;
        // set role list
        authorities = user.getRoles().stream()
                .map(s -> new SimpleGrantedAuthority(s.getName()))
                .collect(Collectors.toList());
        return new UserPrincipal(
                user.getId(),
                user.getPassword(),
                user.getUsername(),
                user.getWallet(),
                authorities
        );
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
