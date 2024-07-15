package com.sparta.hirello.primary.user.repository;

import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Lock(LockModeType.READ)
    @Query("select u from User u where u.username = u :username")
    Optional<User> findByUsernameWithPessimisticLock(Long id);
}
