package com.wayrunny.runway.service;

import com.wayrunny.runway.domain.entity.Course;
import com.wayrunny.runway.domain.dto.course.CourseDto;
import com.wayrunny.runway.domain.dto.course.CourseToViewDto;
import com.wayrunny.runway.domain.entity.Tag;
import com.wayrunny.runway.repository.*;
import com.wayrunny.runway.util.response.error.BadRequestException;
import com.wayrunny.runway.util.response.error.ErrorResponseStatus;
import com.wayrunny.runway.util.response.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TagRepository tagRepository;
    private final GatheringRepository gatheringRepository;

    public List<CourseToViewDto> findAllCourse(){
        return courseRepository.findAllCourseToViewDto();
    }

    public HashMap<String, Object> viewCourseDetailById(String courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(()->new NotFoundException(ErrorResponseStatus.NOT_FOUND_COURSE_EXCEPTION));
        List<Tag> tags = tagRepository.findAllByCourseId(courseId);
        return new LinkedHashMap<>(){{
            put("course_detail", new CourseDto.CourseToDetail(course, tags));
            put("course_gatherings", gatheringRepository.findGatheringListDtoByCourseId(courseId));
        }};
    }

    public List<CourseToViewDto> findCourseViewByTag(String tag) {
        Tag foundTag = tagRepository.findByTagName(tag)
                .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.BAD_REQUEST_WRONG_TAG_EXCEPTION));

        return courseRepository.findCourseToViewByTag(foundTag.getId());
    }

    public List<Course> findCourseByTagId(String tagId){
        return courseRepository.findCourseByTag(tagId);
    }

    public List<CourseToViewDto> findCourseViewByCity(String gu) {
        return courseRepository.findAllCourseToViewDtoByAddressId(gu);
    }
}
