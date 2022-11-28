package com.wayrunny.runway.controller.client;

import com.wayrunny.runway.domain.dto.course.CourseToViewDto;
import com.wayrunny.runway.service.CourseService;
import com.wayrunny.runway.util.response.BaseApiResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @ApiOperation(value = "모든 코스 조회",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "모든 코스 조회 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "모든 코스 조회 성공")
    })
    @GetMapping("/list")
    public BaseApiResponse<Object> viewAllCourse(){

        return new BaseApiResponse<Object>("모든 코스 조회 완료", courseService.findAllCourse());
    }

    @ApiOperation(value = "코스 상세",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "코스 상세 정보 조회 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "코스 상세 정보 조회 성공"),
            @ApiResponse(code = 404, message = "없는 코스 조회")
    })
    @GetMapping("/detail")
    public BaseApiResponse<Object> viewCourseDetail(@RequestParam("course_id") String courseId){

        return new BaseApiResponse<>("ID " + courseId + " 코스 조회 완료",
                courseService.viewCourseDetailById(courseId));
    }

    @ApiOperation(value = "태그로 코스 조회",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "태크로 코스 조회 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "태크로 코스 조회 성공"),
            @ApiResponse(code = 400, message = "잘못된 태그")
    })
    @GetMapping("/search/tag")
    public BaseApiResponse<Object> searchCoursesByTag(@RequestParam("tag") String tag) {

        List<CourseToViewDto> foundCourses = courseService.findCourseViewByTag(tag);

        return new BaseApiResponse<>("Tag " + tag + " 코스 조회 완료", foundCourses);
    }

    @ApiOperation(value = "구로 코스 조회",
            httpMethod = "GET",
            response = BaseApiResponse.class,
            notes = "구로 코스 조회 API"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "구로 코스 조회 성공")
    })
    @GetMapping("/search/city")
    public BaseApiResponse<Object> searchCoursesByCity(@RequestParam("city") String gu){

        return new BaseApiResponse<>("City " + gu + " 코스 조회 완료",
                courseService.findCourseViewByCity(gu));
    }
}
