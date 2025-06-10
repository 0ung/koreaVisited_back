package com.koreavisited.prod.repository;

import com.koreavisited.prod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
