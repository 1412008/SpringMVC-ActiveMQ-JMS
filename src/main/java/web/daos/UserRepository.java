package web.daos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import web.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	@Query("{ 'UserName' : ?0 }")
	public User findByUserName(String us_name);
	
}
