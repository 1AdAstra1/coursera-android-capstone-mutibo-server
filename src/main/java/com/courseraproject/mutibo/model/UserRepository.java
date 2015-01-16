package com.courseraproject.mutibo.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	public User findByName(String name);	
	public User findByExternalIdAndAuthType(String externalId, AuthType authType);
}
