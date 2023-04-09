package com.jpshoppingmall.auth.userdetails;

import com.jpshoppingmall.domain.member.entity.Member;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails, Serializable {
    private Member member;
    public CustomUserDetails(Member member){
        this.member = member;
    }

    public String getName() {
        return member.getName();
    }
    public String getNickname() {
        return member.getNickname();
    }
    public Long getMemberId() {
        return member.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        String prefix = "ROLE_";
        String postfix = member.getRole().name();
        auth.add(new SimpleGrantedAuthority(prefix + postfix));
        return auth;
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
