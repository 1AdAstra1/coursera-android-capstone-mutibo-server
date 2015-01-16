package com.courseraproject.mutibo;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.courseraproject.mutibo.model.User;
import com.courseraproject.mutibo.model.UserRepository;

@Service("userDetailsService")
@Transactional
public class RepositoryUserService implements UserDetailsService {
	@Autowired
	private UserRepository repository;

	public RepositoryUserService() {
		super();
	}


	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = null;

		user = repository.findByName(username);

		if (user == null) {
			throw new UsernameNotFoundException("No user found with name: " + username);
		} 
		return user;
	}

}
