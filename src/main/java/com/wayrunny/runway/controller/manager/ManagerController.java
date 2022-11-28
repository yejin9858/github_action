package com.wayrunny.runway.controller.manager;

import com.wayrunny.runway.config.security.JwtTokenProvider;
import com.wayrunny.runway.domain.dto.RequestDto;
import com.wayrunny.runway.domain.dto.course.CourseLocationDto;
import com.wayrunny.runway.domain.entity.User;
import com.wayrunny.runway.service.FirebaseCloudMessageService;
import com.wayrunny.runway.service.KakaoService;
import com.wayrunny.runway.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

    private final KakaoService kakaoService;
    private final ManagerService managerService;
    private final JwtTokenProvider jwtTokenProvider;
    private final FirebaseCloudMessageService firebaseCloudMessageService;


    @GetMapping("/login/kakao")
    public void  kakaoCallback(@RequestParam String code) {
        System.out.println("controller code :" + code);
        String access_Token = kakaoService.getKakaoAccessToken(code);
        System.out.println("controller access_token :" + access_Token);

        String userId = kakaoService.getUserInfo(access_Token);
        System.out.println(userId);
    }
    @PostMapping(value = "/new/course",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HashMap> addNewCourse(@RequestPart(value = "name") String name,
                                                @RequestPart(value = "content") String content,
                                                @RequestPart(value = "length") String length,
                                                @RequestPart(value = "si") String si,
                                                @RequestPart(value = "gu") String gu,
                                                @RequestPart(value = "dong") String dong,
                                                @RequestPart(value = "image", required = false) MultipartFile image,
                                                @RequestPart(value = "longitude") String longitude,
                                                @RequestPart(value = "latitude") String latitude){

        HashMap<String, Object> responseMap = new HashMap<>();

        managerService.addNewCourse(name, content, Double.parseDouble(length), image, si, gu, dong, Double.parseDouble(longitude), Double.parseDouble(latitude));

        responseMap.put("status", 200);
        responseMap.put("message", "코스 추가 완료");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/new/location")
    public ResponseEntity<HashMap> addNewLocationOfCourse(@RequestBody CourseLocationDto.CourseLocationAndId locationDto){

        HashMap<String, Object> responseMap = new HashMap<>();

        managerService.addNewLocationOfCourse(locationDto);

        responseMap.put("status", 200);
        responseMap.put("message", "코스 경로 위치 추가 완료");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/new/tag")
    public ResponseEntity<HashMap> addNewTag(@RequestPart(value="tag_name") String tagName){
        HashMap<String, Object> responseMap = new HashMap<>();

        managerService.addNewTag(tagName);

        responseMap.put("status", 200);
        responseMap.put("message", "태그 추가 완료");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping(value="/new/user",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HashMap> addNewUser(@RequestPart(value = "nickname") String nickname,
                                              @RequestPart(value = "image", required = false) MultipartFile imageUrl){
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "유저 추가 완료");
        responseMap.put("data",managerService.addTempUser(nickname, imageUrl));
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping(value="/login",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HashMap> tempUserLogin(@RequestPart(value = "nickname") String nickname){
        HashMap<String, Object> responseMap = new HashMap<>();

        User user= managerService.loginTempUser(nickname);
        if (user == null) {
            responseMap.put("status", 400);
            responseMap.put("message", "없는 유저");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            
        }
        else{
            responseMap.put("status", 200);
            responseMap.put("message", "로그인 완료");
            responseMap.put("token", jwtTokenProvider.createToken(user.getSocialId(), user.getRoles()));
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
    }


    @PostMapping("/fcm")
    public ResponseEntity pushMessage(@RequestBody RequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTargetToken() + " "
                +requestDto.getTitle() + " " + requestDto.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody());
        return ResponseEntity.ok().build();
    }

}
