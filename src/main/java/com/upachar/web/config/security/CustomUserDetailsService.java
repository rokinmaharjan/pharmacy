package com.upachar.web.config.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.upachar.web.user.domain.UserRole;
import com.upachar.web.user.domain.User;
import com.upachar.web.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User dbUser = this.userRepository.findByPhone(username);

		if (dbUser != null) {
			Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			
			for (UserRole role : dbUser.getRoles()) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole().toString());
				grantedAuthorities.add(authority);
			}
					
			return new org.springframework.security.core.userdetails.User(
					dbUser.getPhone(), dbUser.getPassword(), grantedAuthorities);
		} else {
			throw new UsernameNotFoundException(String.format("User '%s' not found", username));
		}
	}

}
