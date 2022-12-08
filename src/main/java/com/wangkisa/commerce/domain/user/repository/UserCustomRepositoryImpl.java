package com.wangkisa.commerce.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wangkisa.commerce.domain.user.dto.UserDto;
import com.wangkisa.commerce.domain.user.entity.QUser;
import com.wangkisa.commerce.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.wangkisa.commerce.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<User> findUserByReqSignInDto(UserDto.ReqSignIn dto) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(user)
                .where(
                        user.email.eq(dto.getEmail())
                ).fetchOne());
    }
}
