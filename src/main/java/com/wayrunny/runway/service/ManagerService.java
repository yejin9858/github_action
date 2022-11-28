package com.wayrunny.runway.service;

import com.wayrunny.runway.domain.dto.UserDto;
import com.wayrunny.runway.domain.dto.course.CourseDto;
import com.wayrunny.runway.domain.dto.course.CourseLocation;
import com.wayrunny.runway.domain.dto.course.CourseLocationDto;
import com.wayrunny.runway.domain.entity.Address;
import com.wayrunny.runway.domain.entity.Course;
import com.wayrunny.runway.domain.entity.Tag;
import com.wayrunny.runway.domain.entity.User;
import com.wayrunny.runway.repository.*;
import com.wayrunny.runway.util.ImageUploadService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final CourseRepository courseRepository;
    private final AddressRepository addressRepository;
    private final ImageUploadService imageUploadService;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public void addNewCourse(String name, String content, Double length, MultipartFile image, String si, String gu, String dong, Double longitude, Double latitude){

        Address address = addressRepository.findByGuAndDong(gu, dong).orElse(null);

        if(address ==null){
            address = new Address(si, gu, dong);
        }

        String image_url = null;
        if(!image.isEmpty())
            image_url = imageUploadService.restore(image, "courses");

        CourseDto courseDto = new CourseDto(name, content, image_url, length, longitude, latitude);
        Course newCourse= new Course(courseDto, address);
        address.addNewCourse(newCourse);
        addressRepository.save(address);
    }

    public void addNewLocationOfCourse(CourseLocationDto.CourseLocationAndId locationDto){
        Course course = courseRepository.findById(locationDto.getCourseId()).orElse(null);
        CourseLocation courseLocation = new CourseLocation(course, locationDto.getLongitude(), locationDto.getLatitude());
        course.addNewLocation(courseLocation);
        courseRepository.save(course);
    }

    public void addNewTag(String tagName){
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);
    }

    public User addTempUser(String nickname, MultipartFile imageUrl){
        RandomString randomString = new RandomString(6);
        UserDto userDto = new UserDto();
        userDto.setSocialType("temp");
        if(!imageUrl.isEmpty())
            userDto.setImage_url(imageUploadService.restore(imageUrl, "user"));
        userDto.setSocialId(randomString.nextString());
        userDto.setNickname((nickname == null)?randomString.nextString():nickname);
        userDto.setRoles(Collections.singletonList("ROLE_USER"));

        return userRepository.save(new User(userDto));
    }

    public User loginTempUser(String nickname){
        return userRepository.findByNickname(nickname).orElse(null);
    }

}
