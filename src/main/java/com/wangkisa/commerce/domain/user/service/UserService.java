package com.wangkisa.commerce.domain.user.service;

import com.wangkisa.commerce.domain.user.dto.UserDTO;

public interface UserService {

    UserDTO.ResUserInfo signUp(UserDTO.ReqSignUp reqSignUp);

    UserDTO.ResUserInfo signIn(UserDTO.ReqSignIn reqSignInDto);
}
