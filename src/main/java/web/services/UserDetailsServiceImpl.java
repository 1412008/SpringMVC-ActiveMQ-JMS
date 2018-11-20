package web.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import web.daos.UserRepository;
import web.models.User;

@Service("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository uRepo;
	
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User us = uRepo.findByUserName(userName);
		if (us != null ) {
			System.out.println("UserDetailsService: " + us.toString());
			return new  org.springframework.security.core.userdetails.User(us.getUserName(), us.getPassword(), getAuthorities(us));
		}
		throw new UsernameNotFoundException("Username not found");
	}
	
	private List<GrantedAuthority> getAuthorities (User us) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + us.getType()));
		return authorities;
	}

}
