package com.example.userservice.repository;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

  Optional<UserEntity> findByUserId(String userId);
}
