package com.pyg.cart.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/17 20:20
 */
public class UserDetailsServiceImpl implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<GrantedAuthority> authentication=new ArrayList<>();
		authentication.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new User(username,"", authentication);
	}
}
