package com.wangkisa.commerce.domain.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

//    private final JPAQueryFactory jpaQueryFactory;
//
//    public Optional<User> findUserByReqSignInDto(UserDto.ReqSignIn dto) {
//        return Optional.ofNullable(jpaQueryFactory.selectFrom(user)
//                .where(
//                        user.email.eq(dto.getEmail())
//                ).fetchOne());
//    }
}
