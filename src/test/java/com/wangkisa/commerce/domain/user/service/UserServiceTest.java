package com.wangkisa.commerce.domain.user.service;

import com.wangkisa.commerce.domain.user.code.UserErrorCode;
import com.wangkisa.commerce.domain.user.dto.UserDto;
import com.wangkisa.commerce.domain.user.entity.User;
import com.wangkisa.commerce.domain.user.repository.UserRepository;
import com.wangkisa.commerce.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
//        userService = new UserServiceImpl(userRepository, jwtUtils, passwordEncoder);
//        userService = new UserServiceImpl();
    }

    private UserDto.ReqSignUp reqSignUpUserDto() {
        return UserDto.ReqSignUp.builder()
                .email("test@test.com")
                .nickName("테스트 닉네임")
                .phone("010-1234-5678")
                .password("testtest")
                .build();
    }

    @Test
    @DisplayName("이메일이 중복인 경우 회원가입 실패 테스트")
    public void signUp_EmailDup_FailTest() {
        // given
        UserDto.ReqSignUp defaultReqSignUpDto = reqSignUpUserDto();
        when(userRepository.existsByEmail(defaultReqSignUpDto.getEmail())).thenReturn(true);

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signUp(defaultReqSignUpDto));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_EMAIL.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_EMAIL.getMsg());
    }

    @Test
    @DisplayName("닉네임이 중복인 경우 회원가입 실패 테스트")
    public void signUp_NickNameDup_FailTest() {
        // given
        UserDto.ReqSignUp defaultReqSignUpDto = reqSignUpUserDto();
        when(userRepository.existsByNickName(defaultReqSignUpDto.getNickName())).thenReturn(true);

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signUp(defaultReqSignUpDto));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_NICKNAME.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_NICKNAME.getMsg());
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void signUp_SucTest() {
        // given
        UserDto.ReqSignUp defaultReqSignUpDto = reqSignUpUserDto();

        // when
        UserDto.ResUserInfo resUserInfo = userService.signUp(defaultReqSignUpDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));

        Assertions.assertThat(resUserInfo.getEmail()).isEqualTo(defaultReqSignUpDto.getEmail());
        Assertions.assertThat(resUserInfo.getNickName()).isEqualTo(defaultReqSignUpDto.getNickName());
        Assertions.assertThat(resUserInfo.getPhone()).isEqualTo(defaultReqSignUpDto.getPhone());
    }


    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() {
        /**
         * 1. 인증 번호값이 오류 인 경우 실패 테스트
         */
        // given

        // when

        // then
        /**
         * 2. 이메일 혹은 닉네임이 중복인 경우 실패 테스트
         */
        // given

        // when

        // then

        /**
         * 3. 휴대폰 인증 정상과 가입 성공 테스트
         */
        // given

        // when

        // then

//        userService.signUp()
    }

    @Test
    public void $NAME() {

    }
}