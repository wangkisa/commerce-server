package com.wangkisa.commerce.domain.user.entity;

import com.wangkisa.commerce.domain.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "users", indexes = {})
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Embedded
    @Column(nullable = false)
    private Password password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String phone;

    private String refreshToken;

    @Column(precision = 10, scale = 0)
    private BigDecimal point;

    @Builder
    public User(String email, Password password, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.point = BigDecimal.ZERO;
    }

    public void chargePoint(BigDecimal point){
        this.point = this.point.add(point);
    }
}
