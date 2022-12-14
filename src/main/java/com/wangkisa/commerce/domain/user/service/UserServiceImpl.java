package com.wangkisa.commerce.domain.user.service;

import com.wangkisa.commerce.domain.jwt.JwtTokenProvider;
import com.wangkisa.commerce.domain.user.code.UserErrorCode;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
import com.wangkisa.commerce.domain.user.entity.Password;
import com.wangkisa.commerce.domain.user.entity.User;
import com.wangkisa.commerce.domain.user.repository.UserRepository;
import com.wangkisa.commerce.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    private boolean checkDuplicateNickName(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

    /**
     * 이메일 회원가입
     */
    @Override
    @Transactional
    public UserDTO.ResUserInfo signUp(UserDTO.ReqSignUp reqSignUp) {

        if (checkDuplicateEmail(reqSignUp.getEmail())) {
            throw new CustomException(UserErrorCode.ERROR_SIGNUP_DUPLICATE_EMAIL);
        }
        if (checkDuplicateNickName(reqSignUp.getNickName())) {
            throw new CustomException(UserErrorCode.ERROR_SIGNUP_DUPLICATE_NICKNAME);
        }

        User user = User.builder()
                .email(reqSignUp.getEmail())
                .nickname(reqSignUp.getNickName())
                .phone(reqSignUp.getPhone())
                .password(Password.of(reqSignUp.getPassword(), passwordEncoder))
                .build();

        User savedUser = userRepository.save(user);

        return UserDTO.ResUserInfo.fromUser(savedUser, null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO.ResUserInfo signIn(UserDTO.ReqSignIn reqSignInDto) {

        User findUser = userRepository.findByEmail(reqSignInDto.getEmail())
                .filter(it -> it.getPassword().matchesPassword(reqSignInDto.getPassword(), passwordEncoder))
                .orElseThrow(() -> new CustomException(UserErrorCode.ERROR_NOT_FOUND_USER_INFO));

        final var jwtModel = jwtTokenProvider.createToken(findUser.getEmail());

        return UserDTO.ResUserInfo.fromUser(findUser, jwtModel);
    }
}
