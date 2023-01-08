package com.wangkisa.commerce.domain.user.service;

import com.wangkisa.commerce.configuration.TestConfig;
import com.wangkisa.commerce.domain.user.code.UserErrorCode;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
import com.wangkisa.commerce.domain.user.repository.UserRepository;
import com.wangkisa.commerce.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(TestConfig.class)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {

    }

    private UserDTO.ReqSignUp getReqSignUpUserDTO() {
        return UserDTO.ReqSignUp.builder()
                .email("test@test.com")
                .nickName("테스트 닉네임")
                .phone("010-1234-5678")
                .password("testtest")
                .build();
    }

    @Test
    @DisplayName("이메일이 중복인 경우 회원가입 실패 테스트")
    @Transactional
    public void signUp_EmailDup_FailTest() {
        // given
        UserDTO.ReqSignUp defaultReqSignUpDto = getReqSignUpUserDTO();

        UserDTO.ReqSignUp reqSignUpDto = UserDTO.ReqSignUp.builder()
                .email(defaultReqSignUpDto.getEmail())
                .nickName("테스트 닉네임2")
                .phone("010-1234-5678")
                .password("testtest")
                .build();

        // defaultReqSignUpDto의 이메일 test@test.com 를 먼저 회원가입
        UserDTO.ResUserInfo resUserInfo = userService.signUp(reqSignUpDto);

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signUp(defaultReqSignUpDto));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_EMAIL.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_EMAIL.getMsg());
    }

    @Test
    @DisplayName("닉네임이 중복인 경우 회원가입 실패 테스트")
    @Transactional
    public void signUp_NickNameDup_FailTest() {
        // given
        UserDTO.ReqSignUp defaultReqSignUpDto = getReqSignUpUserDTO();

        UserDTO.ReqSignUp reqSignUpDto = UserDTO.ReqSignUp.builder()
                .email("new@test.com")
                .nickName(defaultReqSignUpDto.getNickName())
                .phone("010-1234-5678")
                .password("testtest")
                .build();

        // defaultReqSignUpDto의 닉네임 테스트 닉네임을 먼저 회원가입
        UserDTO.ResUserInfo resUserInfo = userService.signUp(reqSignUpDto);

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signUp(defaultReqSignUpDto));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_NICKNAME.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_NICKNAME.getMsg());
    }

    @Test
    @DisplayName("이메일 혹은 비밀번호 틀린 경우 경우 회원로그인 실패 테스트")
    @Transactional
    public void signIn_WrongEmailPw_FailTest() {
        // given
        UserDTO.ReqSignUp defaultReqSignUpDto = getReqSignUpUserDTO();
        // 기본정보 회원 가입
        userService.signUp(defaultReqSignUpDto);

        // 기존 비밀번호에 "wrong" 문자 추가
        UserDTO.ReqSignIn defaultReqSignInDto = UserDTO.ReqSignIn.builder()
                .email("test@test.com")
                .password(defaultReqSignUpDto.getPassword() + "wrong")
                .build();

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signIn(defaultReqSignInDto));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(UserErrorCode.ERROR_NOT_FOUND_USER_INFO.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(UserErrorCode.ERROR_NOT_FOUND_USER_INFO.getMsg());
    }


    @Test
    @DisplayName("회원가입 성공 테스트")
    @Transactional
    public void signUpTest() {
        // given
        UserDTO.ReqSignUp defaultReqSignUpDto = getReqSignUpUserDTO();

        // when
        UserDTO.ResUserInfo resUserInfo = userService.signUp(defaultReqSignUpDto);

        // then
        Assertions.assertThat(resUserInfo.getEmail()).isEqualTo(defaultReqSignUpDto.getEmail());
        Assertions.assertThat(resUserInfo.getNickname()).isEqualTo(defaultReqSignUpDto.getNickName());
        Assertions.assertThat(resUserInfo.getPhone()).isEqualTo(defaultReqSignUpDto.getPhone());
    }
}