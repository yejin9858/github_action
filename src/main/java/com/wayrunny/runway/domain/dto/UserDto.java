package com.wayrunny.runway.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.entity.User;
import com.wayrunny.runway.util.KeyService;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    @NotNull
    private String nickname;

    private String image_url;

    @NotNull
    private String socialType; //kakao. apple

    @NotNull
    private String socialId; //id


    private List<String> roles = new ArrayList<>();


    @Getter
    @Setter
    public static class LoginDto {
        @NotNull
        private String socialType;

        @NotNull
        private String socialToken;
    }


    @Getter
    @Setter
    public static class SignUpDto{
        @NotNull
        private String socialType;

        @NotNull
        private String socialToken;

        @NotNull
        private String nickname;
    }

    @Getter
    public static class ProfileDto {
        @NotNull
        private String nickname;

        @NotNull
        @JsonProperty("image_url")
        private String imageUrl;

        @NotNull
        private Integer level;

        @NotNull
        private Integer exp;


        public ProfileDto(User user){
            nickname = user.getNickname();
            imageUrl = KeyService.addBaseURLToImage(user.getImageUrl());
            level = user.getLevel();
            exp = user.getExp();
        }
    }


}
