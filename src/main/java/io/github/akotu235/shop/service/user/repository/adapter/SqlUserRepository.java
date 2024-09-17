package io.github.akotu235.shop.service.user.repository.adapter;

import io.github.akotu235.shop.service.user.model.User;
import io.github.akotu235.shop.service.user.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlUserRepository extends UserRepository, JpaRepository<User, Integer> {
}