package com.wangkisa.commerce.domain.user.service;

import com.wangkisa.commerce.domain.user.code.UserErrorCode;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
    public void signUp_EmailDup_FailTest() {
        // given
        UserDTO.ReqSignUp defaultReqSignUpDto = getReqSignUpUserDTO();
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
        UserDTO.ReqSignUp defaultReqSignUpDto = getReqSignUpUserDTO();
        when(userRepository.existsByNickname(defaultReqSignUpDto.getNickName())).thenReturn(true);

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signUp(defaultReqSignUpDto));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_NICKNAME.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(UserErrorCode.ERROR_SIGNUP_DUPLICATE_NICKNAME.getMsg());
    }

    @Test
    @DisplayName("이메일 혹은 비밀번호 틀린 경우 경우 회원로그인 실패 테스트")
    public void signIn_WrongEmailPw_FailTest() {
        // given
        UserDTO.ReqSignIn defaultReqSignInDto = UserDTO.ReqSignIn.builder()
                .email("test@test.com")
                .password("testtest")
                .build();

//        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signIn(defaultReqSignInDto));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(UserErrorCode.ERROR_NOT_FOUND_USER_INFO.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(UserErrorCode.ERROR_NOT_FOUND_USER_INFO.getMsg());
    }


}