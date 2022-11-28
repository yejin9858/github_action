package com.wayrunny.runway.controller.client;

import com.wayrunny.runway.config.security.JwtTokenProvider;
import com.wayrunny.runway.domain.dto.UserDto;
import com.wayrunny.runway.domain.entity.User;
import com.wayrunny.runway.service.UserService;
import com.wayrunny.runway.util.response.BaseApiResponse;
import com.wayrunny.runway.util.response.BaseApiResponseNonData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;



@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "로그인",
            httpMethod = "POST",
            response = BaseApiResponse.class,
            notes = "소셜 로그인 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 401, message = "소셜 타입 오류"),
            @ApiResponse(code = 404, message = "회원 정보 없음")
    })
    @PostMapping("/users/login")
    public BaseApiResponse<Object> socialLogin(@RequestBody UserDto.LoginDto loginDto) throws ParseException {

        User user = userService.signIn(loginDto.getSocialType(), loginDto.getSocialToken());

        String socialType = loginDto.getSocialType();
        return new BaseApiResponse<Object>(socialType + "로그인 성공",
                new HashMap<String, String>() {{
                    put("token", jwtTokenProvider.createToken(user.getSocialId(), user.getRoles()));
                }}
        );

    }

    @ApiOperation(value = "회원가입",
            httpMethod = "POST",
            response = BaseApiResponse.class,
            notes = "소셜 로그인에 실패했을 경우, 소셜 회원 가입 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원가입 성공"),
            @ApiResponse(code = 401, message = "소셜 토큰 오류"),
            @ApiResponse(code = 409, message = "닉네임 중복")
    })
    @PostMapping("/users/signup")
    public BaseApiResponse<Object> socialSignUp(@RequestBody UserDto.SignUpDto signUpDto) throws ParseException {

        userService.validateDuplicateNickname(signUpDto.getNickname());

        User user = userService.signUp(signUpDto.getSocialType(), signUpDto.getSocialToken(), signUpDto.getNickname());

        return new BaseApiResponse<Object>("회원가입 성공",
                new HashMap<String, String>() {{
                    put("token", jwtTokenProvider.createToken(user.getSocialId(), user.getRoles()));
                }}
        );
    }

    @ApiOperation(value = "회원탈퇴",
            httpMethod = "POST",
            response = BaseApiResponse.class,
            notes = "회원 탈퇴"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 탈퇴 ")
    })
    @PostMapping("/users/signout")
    public BaseApiResponseNonData<Object>socialSignOut(@RequestHeader("X-AUTH-TOKEN") String token) {

        userService.signOut(token);
        return new BaseApiResponseNonData<Object>("회원탈퇴 성공");
    }

    @ApiOperation(value = "마이페이지",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "마이페이지 정보 불러오기 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "마이페이지 정보 불러오기 성공")
    })
    @GetMapping(value = "/users/mypage")
    public BaseApiResponse<Object> getProfile(@RequestHeader("X-AUTH-TOKEN") String token) {

        User user = userService.findUserBySocialId(jwtTokenProvider.getUserSocialId(token));
        UserDto.ProfileDto profileDto = new UserDto.ProfileDto(user);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "조회 완료");
        return new BaseApiResponse<Object>("마이페이지 조회 완료",
                new LinkedHashMap<String, Object>() {{
                    put("information", profileDto);
                    put("running_data", userService.getUserRunningNumericData(user));
                    put("running_stat", userService.getUserRunningStats(user));
                    put("schedule", userService.getCloseRunningSchedule(user));
                }}
        );
    }

    @ApiOperation(value = "프로필 정보 변경",
            httpMethod = "PUT",
            response = BaseApiResponseNonData.class,
            notes = "프로필 정보 변경 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "프로필 변경 성공"),
            @ApiResponse(code = 409, message = "5MB이하의 이미지만 등록할 수 있습니다.")
    })
    @PutMapping(value = "/users/profile/edit", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseApiResponseNonData<Object> updateProfile(@RequestHeader("X-AUTH-TOKEN") String token,
                                                 @RequestPart(value="nickname") String nickname,
                                                 @RequestPart(value ="image_url", required = false) MultipartFile image) {

        User user = userService.findUserBySocialId(jwtTokenProvider.getUserSocialId(token));
        userService.updateProfile(user, nickname, image);

        return new BaseApiResponseNonData("프로필 업데이트 성공");
    }

    @ApiOperation(value = "예약된 번개방 모두 보기",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "예약된 번개방 리스트 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "예약된 번개방 조회 성공")
    })
    @GetMapping( "/users/gathering-list")
    public BaseApiResponse<Object> reservedGathering(@RequestHeader("X-AUTH-TOKEN") String token) {

        User user = userService.findUserBySocialId(jwtTokenProvider.getUserSocialId(token));

        return new BaseApiResponse("프로필 업데이트 성공",
                userService.lookupReservedGathering(user));
    }
}
