package com.bmdb.business;

import org.springframework.data.repository.CrudRepository;

public interface ActorRepository extends  CrudRepository<Actor, Integer> { 
	Actor findByGender(String gender);

}
