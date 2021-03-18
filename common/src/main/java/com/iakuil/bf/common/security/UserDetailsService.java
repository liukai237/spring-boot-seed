package com.iakuil.bf.common.security;

public interface UserDetailsService {

    UserDetails getCurrentUser();

    UserDetails loadUserByUsername(String username);
}
