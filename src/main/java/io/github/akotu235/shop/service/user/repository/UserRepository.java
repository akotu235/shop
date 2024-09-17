package io.github.akotu235.shop.service.user.repository;

import io.github.akotu235.shop.service.user.model.User;

public interface UserRepository {

    User findByEmail(String email);

    User findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User save(User entity);
}
