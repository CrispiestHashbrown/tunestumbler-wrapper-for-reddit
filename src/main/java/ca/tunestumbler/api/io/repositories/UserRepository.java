package ca.tunestumbler.api.io.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ca.tunestumbler.api.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
	UserEntity findByEmail(String email);
	
	UserEntity findByUserId(String userId);
	
	UserEntity findByToken(String token);
	
	UserEntity findByRefreshToken(String refreshToken);
}
