package ca.tunestumbler.api.io.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.tunestumbler.api.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
	UserEntity findByEmail(String email);
	
	UserEntity findByUserId(String userId);
	
	@Query(value = "SELECT email FROM users WHERE user_id = :userId", nativeQuery = true)
	String findEmailByUserId(@Param("userId") String userId);
}
