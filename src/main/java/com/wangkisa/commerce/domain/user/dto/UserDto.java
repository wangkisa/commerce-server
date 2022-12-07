package com.wangkisa.commerce.domain.user.dto;

import com.wangkisa.commerce.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReqSignUp {

        @NotNull
        @Email
        @Schema(description = "이메일")
        private String email;

        @NotNull
        @Size(min = 1, max = 8, message = "닉네임은 8글자를 초과할 수 없습니다.")
        @Schema(description = "닉네임")
        private String nickName;

        @NotBlank
        @Size(min = 6, max = 12, message = "비밀번호는 6글자에서 12글자 사이로 입력해야 합니다.")
        @Schema(description = "비밀번호")
        private String password;

        @NotNull
        @Pattern(regexp = "^[0-9]{3}[-]+[0-9]{4}[-]+[0-9]{4}", message = "전화번호 형식은 000-0000-0000 입니다.")
        @Schema(description = "휴대폰 번호(000-0000-0000)")
        private String phone;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ResUserInfo {
        @Schema(description = "유저 아이디")
        private Long userId;

        @Schema(description = "이메일")
        private String email;

        @Schema(description = "닉네임")
        private String nickname;

        @Schema(description = "휴대폰 번호")
        private String phone;

        public static ResUserInfo fromUser(User user) {
            return ResUserInfo.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .phone(user.getPhone())
                    .build();
        }
    }
}
