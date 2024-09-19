package com.e22e.moya.user.repository;

import com.e22e.moya.common.entity.Users;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    Optional<Users> findById(long id);
    Optional<Users> findByEmail(String email);
}
