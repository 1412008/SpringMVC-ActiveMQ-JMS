package web.daos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import web.models.Switch;

@Repository
public interface SwitchRepository extends MongoRepository<Switch, String> {
	
	@Query("{ 'MAC' : ?0 }")
	public Switch findByMAC(String mac);
	
}
