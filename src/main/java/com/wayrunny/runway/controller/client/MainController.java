package com.wayrunny.runway.controller.client;

import com.wayrunny.runway.service.MainService;
import com.wayrunny.runway.util.WeatherService;
import com.wayrunny.runway.util.response.BaseApiResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;
    private final WeatherService weatherService;

    @ApiOperation(value = "메인",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "메인 뷰 정보 불러오기 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "메인 뷰 정보 불러오기 성공")
    })
    @GetMapping()
    public BaseApiResponse<Object> mainView() {

        return new BaseApiResponse<>("메인 뷰 정보 조회 완료", mainService.getCoursesOnMainView());
    }

    @ApiOperation(value = "좌표로 근처 코스 찾기",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "좌표로 근처 코스 찾기 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "좌표 근처 코스 찾기 성공")
    })
    @GetMapping("/search/nearby")
    public BaseApiResponse<Object> searchNearCourses(@RequestParam("latitude")Double latitude,
                                                     @RequestParam("longitude")Double longitude) {

        HashMap<String, Object> courseAnswersToLocation = mainService.getNearCoursesToLocation(latitude, longitude);

        return new BaseApiResponse<>("좌표 근처 코스 찾기 완료",
                new LinkedHashMap<String, Object>(){{
                    put("weather", weatherService.getWeather(latitude, longitude));
                    put("near_courses", courseAnswersToLocation.get("near_courses"));
                    put("other_courses",courseAnswersToLocation.get("other_courses"));
                }}
        );
    }
}
