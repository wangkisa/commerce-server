package com.wangkisa.commerce.domain.user.repository;

import com.wangkisa.commerce.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickName);

    Optional<User> findByEmail(String email);
}
