package com.nnutalap.MyHomeProjectDb.repository;

import com.nnutalap.MyHomeProjectDb.models.MyUsers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//@Component
public interface MhpUsersDbRepo extends MongoRepository<MyUsers, Integer>{
	
	Optional<MyUsers> findByEmail(String email);

}
