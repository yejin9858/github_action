package com.wayrunny.runway.service;

import com.wayrunny.runway.domain.entity.Course;
import com.wayrunny.runway.domain.dto.course.CourseMainDto;
import com.wayrunny.runway.domain.entity.Tag;
import com.wayrunny.runway.repository.CourseRepository;
import com.wayrunny.runway.repository.TagRepository;
import com.wayrunny.runway.util.MainMentSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Math.*;

@Service
@RequiredArgsConstructor
public class MainService {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final TagRepository tagRepository;

    private final Integer courseOnMainStyleArray[] = {0, 1, 1};

    public CourseMainDto getCoursesOnMainView(){
        CourseMainDto courseMainDto = new CourseMainDto();

        Integer mainImageCount = 3;
        List<CourseMainDto.CourseBoxSetWithGu> mainCourseImage = new ArrayList<>();
        List<Course> coursesForImage = pickRandomCoursesForMainImage(mainImageCount);
        for(int i =0 ; i < coursesForImage.size() ; i++){
            mainCourseImage.add(new CourseMainDto.CourseBoxSetWithGu(coursesForImage.get(i)));
        }
        courseMainDto.setImageOnMains(mainCourseImage);

        Integer mainTopicCount = 3;
        List<String> tagIdsForTopic = pickRandomQuestionTagId(mainTopicCount);



        for(int i = 0 ; i < mainTopicCount ; i++){
            HashMap<String, Object> mainTopicCourseSet = new LinkedHashMap<>();

            String nowTopicId = tagIdsForTopic.get(i);
            mainTopicCourseSet.put("topic", MainMentSet.checkList.get(nowTopicId));
            List<Course> courses = courseService.findCourseByTagId(nowTopicId);

            if(courseOnMainStyleArray[i] == 0){
                List<CourseMainDto.CourseBoxSetWithGu> courseBoxSet = new ArrayList<>();
                for(Course c : courses){
                    CourseMainDto.CourseBoxSetWithGu mainSet = new CourseMainDto.CourseBoxSetWithGu(c);
                    courseBoxSet.add(mainSet);
                }
                mainTopicCourseSet.put("courses", courseBoxSet);
            }
            else if(courseOnMainStyleArray[i] ==1){
                List<CourseMainDto.CourseBoxSetWithNameLength> courseBoxSet = new ArrayList<>();
                for(Course c : courses){
                    CourseMainDto.CourseBoxSetWithNameLength mainSet = new CourseMainDto.CourseBoxSetWithNameLength(c);
                    courseBoxSet.add(mainSet);
                }
                mainTopicCourseSet.put("courses", courseBoxSet);
            }
            courseMainDto.addCoursesWithTopic(mainTopicCourseSet);

        }
        return courseMainDto;
    }

    public HashMap<String, Object> getNearCoursesToLocation(Double latitude, Double longitude){
        HashMap <String, Object> nearCoursesToLocationResult = new HashMap<>();

        List<CourseMainDto.CourseBoxSetWithNameLength> nearCourseList = new ArrayList<>();

        List<Course> notFarCourses = courseRepository.findNearCourseFrom(latitude, longitude);
        List<Course> farCourses = courseRepository.findAll();
        System.out.println(farCourses.size());

        Double distance = 3.5;
        for(Course c : notFarCourses){
            if (calculateDistance(c.getLatitude(), c.getLongitude(), latitude, longitude) < distance){
                nearCourseList.add(new CourseMainDto.CourseBoxSetWithNameLength(c));
                farCourses.remove(c);
            }
        }
        nearCoursesToLocationResult.put("near_courses", nearCourseList);
        System.out.println(farCourses.size());

        Integer otherRegionCoursesCount = 3;
        List<CourseMainDto.CourseBoxSetWithGu> otherRegionCourses = new ArrayList<>();
        if(farCourses.size() > otherRegionCoursesCount) {
            List<Integer> randomNumber = pickRandomNumber(otherRegionCoursesCount, farCourses.size());
            for (int i = 0; i < otherRegionCoursesCount; i++) {
                otherRegionCourses.add(new CourseMainDto.CourseBoxSetWithGu(farCourses.get(randomNumber.get(i))));
            }
        }
        nearCoursesToLocationResult.put("other_courses", otherRegionCourses);

        return nearCoursesToLocationResult;
    }

    private Double calculateDistance(Double latitude1, Double longitude1, Double latitude2, Double longitude2){
        Double earthRadius =6376.5;
        Double distanceLatitudeRadius1 = latitude1 *(PI / 180.0);
        Double distanceLongitudeRadius1 = longitude1 * (PI / 180.0);
        Double distanceLatitudeRadius2 = latitude2 * (PI / 180.0);
        Double distanceLongitudeRadius2 = longitude2 *(PI / 180.0);

        Double distanceLatitude = distanceLatitudeRadius2 - distanceLatitudeRadius1;
        Double distanceLongitude = distanceLongitudeRadius2 - distanceLongitudeRadius1;

        Double temp = pow(sin(distanceLatitude / 2.0), 2.0) +
                cos(distanceLatitudeRadius1) * cos(distanceLatitudeRadius2) *
                        pow(sin(distanceLongitude / 2.0), 2.0);
        Double unitSphereDistance =2.0 * atan2(sqrt(temp), sqrt(1.0 - temp));
        return earthRadius * unitSphereDistance;
    }

    private List<String> pickRandomQuestionTagId(Integer mainTopicCount){

        List<Tag> allTags = tagRepository.findAll();
        Integer allTagsCount = allTags.size();

        List<Integer> randomNumberSet = pickRandomNumber(mainTopicCount, allTagsCount);

        List<String> randomQuestionIdSet = new ArrayList<>();

        for(int i = 0 ; i < randomNumberSet.size() ; i++){
            randomQuestionIdSet.add(allTags.get(randomNumberSet.get(i)).getId());
        }

        return randomQuestionIdSet;
    }

    private List<Course> pickRandomCoursesForMainImage(Integer mainImageCount){

        List<Course> allCourses = courseRepository.findAll();
        Integer allCoursesCount = allCourses.size();

        List<Course> randomCourseSet = new ArrayList<>();

        List<Integer> randomNumberSet = pickRandomNumber(mainImageCount, allCoursesCount);

        for(int i = 0 ; i < randomNumberSet.size() ; i++){
            randomCourseSet.add(allCourses.get(randomNumberSet.get(i)));
        }

        return randomCourseSet;
    }

    private List<Integer> pickRandomNumber(Integer count, Integer max){

        Boolean randomNumberPicked[] = new Boolean[max];
        Arrays.fill(randomNumberPicked, false);
        List<Integer> randomNumberSet = new ArrayList<>();

        for(int i = 0 ; i < count ;i++){
            Integer randomNumber = (int)(Math.random() * 100) % 13;
            while(randomNumberPicked[randomNumber]){
                randomNumber  = (int)(Math.random() * 100) % 13;
            }
            randomNumberPicked[randomNumber] = true;
            randomNumberSet.add(randomNumber);
        }

        return randomNumberSet;
    }
}
