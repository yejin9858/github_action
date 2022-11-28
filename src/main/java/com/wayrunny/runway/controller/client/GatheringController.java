package com.wayrunny.runway.controller.client;

import com.wayrunny.runway.config.security.JwtTokenProvider;
import com.wayrunny.runway.domain.dto.gathering.GatheringDto;
import com.wayrunny.runway.domain.dto.gathering.GatheringPWDto;
import com.wayrunny.runway.domain.dto.gathering.UserRunningRecordDto;
import com.wayrunny.runway.domain.entity.User;
import com.wayrunny.runway.service.GatheringService;
import com.wayrunny.runway.service.UserService;
import com.wayrunny.runway.util.response.BaseApiResponse;
import com.wayrunny.runway.util.response.BaseApiResponseNonData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;


@RestController
@RequiredArgsConstructor
@RequestMapping("/gathering")
public class GatheringController {

    private final GatheringService gatheringService;
    private  final  UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "번개방 등록",
            httpMethod = "POST",
            response = BaseApiResponse.class,
            notes = "번개방 등록 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "방 등록 완료"),
            @ApiResponse(code = 400, message = "잘못된 요청. 빠진 것이 있는지 확인해주세요.")
    })
    @PostMapping("/create")
    public BaseApiResponseNonData<Object> createGathering(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody GatheringDto.GatheringCreateDto gatheringCreateDto) {

        User user = userService.findUserBySocialId(jwtTokenProvider.getUserSocialId(token));
        gatheringService.createGathering(gatheringCreateDto, user);

        return new BaseApiResponseNonData<>("번개방 등록 완료");
    }


    @ApiOperation(value = "번개방 최신순 조회",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "번개장 최신 생성방 찾기 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "최신순 번개방 조회 완료")
    })
    @GetMapping("/list/new")
    public BaseApiResponse<Object> viewNewGatherings() {

        return new BaseApiResponse<>("최신 번개방 찾기 완료", gatheringService.getNewGatherings());
    }


    @ApiOperation(value = "번개방 빠른 참여 가능순 조회",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "번개방 빠른 참여 가능순 찾기 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "빠른 참여 가능한 번개방 조회 완료")
    })
    @GetMapping("/list/fast")
    public BaseApiResponse<Object> viewNearGatherings() {

        return new BaseApiResponse<>("번개방 빠른 참여순 찾기 완료",
                gatheringService.getFastParticipatedGathering());
    }

    @ApiOperation(value = "번개방 상세 조회",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "번개방 상세 조회 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "번개방 상세 정보 조회 완료"),
            @ApiResponse(code = 404, message = "번개방 정보 없음")
    })
    @GetMapping("/detail")
    public BaseApiResponse<Object> viewGatheringDetail(@RequestParam("gathering_id") String gatheringIdString) {

        Long gatheringId = Long.parseLong(gatheringIdString);
        return new BaseApiResponse<>("ID " + gatheringId + "조회 완료",
                gatheringService.viewGatheringDetail(gatheringId));
    }

    @ApiOperation(value = "번개방 참여하기",
            httpMethod = "POST",
            response = BaseApiResponseNonData.class,
            notes = "번개방 참여하기 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "번개방 참여하기 성공"),
            @ApiResponse(code = 409, message = "이미 참가하고 있거나 참여할 수 없는 방")
    })
    @PostMapping("/join")
    public BaseApiResponseNonData<Object> joinGathering(@RequestHeader("X-AUTH-TOKEN") String token,
                                                        @RequestBody GatheringDto.GatheringJoinDto joinDto) {
        User user = userService.findUserBySocialId(jwtTokenProvider.getUserSocialId(token));
        gatheringService.joinGathering(user.getId(), joinDto.getGatheringId());
        return new BaseApiResponseNonData<>("ID " + user.getId() + " " + joinDto.getGatheringId() + "번방에 참여 완료");
    }

    @ApiOperation(value = "번개방 비밀번호 일치여부 확인",
            httpMethod = "POST",
            response = BaseApiResponse.class,
            notes = "번개방 비밀번호 일치여부 확인 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "번개방 비밀번호 일치")
    })
    @PostMapping("/list/checkpw")
    public BaseApiResponse<Object> checkGatheringsPW(@RequestBody GatheringPWDto gatheringPWDto) {


        return new BaseApiResponse<>("번개방 비밀번호 일치",
                gatheringService.checkGatheringPassword(gatheringPWDto));
    }

    @ApiOperation(value = "번개방 이름으로 검색",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "번개방 이름으로 검색 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "번개방 검색 완료")
    })
    @GetMapping("/search")
    public BaseApiResponse<Object> searchGathering(@RequestParam("keyword")String keyword) {

        return new BaseApiResponse<>("번개방 " + keyword + "로 검색 완료",
                gatheringService.searchGatheringByName(keyword));
    }

    @ApiOperation(value = "번개방/ 개인 러닝 참여 인증",
            httpMethod = "POST",
            response = BaseApiResponseNonData.class,
            notes = "번개방/ 개인 러닝 참여 인증 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "번개방 참여 인증 완료"),
            @ApiResponse(code = 409, message = "이미 참여가 완료된 번개방"),
            @ApiResponse(code = 404, message = "번개방 정보 없음")
    })
    @PostMapping("/attendance")
    public BaseApiResponseNonData<Object> certifyAttendance(@RequestHeader("X-AUTH-TOKEN") String token,
                                                            @RequestBody UserRunningRecordDto recordDto) {

        User user = userService.findUserBySocialId(jwtTokenProvider.getUserSocialId(token));

        return new BaseApiResponseNonData<>(gatheringService.attendanceGathering(user, recordDto));
    }

    @ApiOperation(value = "번개방 러닝 인증을 위한 번개방 목록",
            httpMethod = "POST",
            response = BaseApiResponse.class,
            notes = "번개방/ 개인 러닝 참여 인증 위한 목록 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "번개방, 코스 조회 성공")
    })
    @GetMapping("/attendance/list")
    public BaseApiResponse<Object> getGatheringAndCourseListForAuthorization(@RequestHeader("X-AUTH-TOKEN") String token) {

        User user = userService.findUserBySocialId(jwtTokenProvider.getUserSocialId(token));

        return new BaseApiResponse<>("번개방, 코스 조회 성공",
                        new LinkedHashMap<String, Object>(){{
                        put("now_gathering_list", gatheringService.getNowGatherings(user));
                    }}
                );
    }



}
