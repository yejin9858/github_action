package com.wayrunny.runway.service;

import com.wayrunny.runway.config.security.JwtTokenProvider;
import com.wayrunny.runway.domain.dto.UserDto;
import com.wayrunny.runway.domain.dto.gathering.GatheringLookUpDto;
import com.wayrunny.runway.domain.dto.gathering.GatheringRecordDto;
import com.wayrunny.runway.domain.dto.gathering.RunningRecordDto;
import com.wayrunny.runway.domain.entity.User;
import com.wayrunny.runway.repository.GatheringRepository;
import com.wayrunny.runway.repository.UserGatheringRelationRepository;
import com.wayrunny.runway.repository.UserRepository;
import com.wayrunny.runway.repository.UserRunningRecordRepository;
import com.wayrunny.runway.util.ImageUploadService;
import com.wayrunny.runway.util.KeyService;
import com.wayrunny.runway.util.response.error.ConflictException;
import com.wayrunny.runway.util.response.error.ErrorResponseStatus;
import com.wayrunny.runway.util.response.error.NotFoundException;
import com.wayrunny.runway.util.response.error.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImageUploadService imageUploadService;
    private final UserRunningRecordRepository userRunningRecordRepository;
    private final UserGatheringRelationRepository userGatheringRelationRepository;
    private final GatheringRepository gatheringRepository;

    public User signIn(String social_type, String social_token) throws ParseException {

        String id = null;

        if (social_type.equals("kakao")) {
            id = kakaoService.getUserInfo(social_token);
        } else if (social_type.equals("apple")) {
            id = appleService.userIdFromApple(social_token);
        }

        if(id == null)
            throw new NotFoundException(ErrorResponseStatus.NOT_FOUND_USER_EXCEPTION);

        User user = findUserBySocialTypeAndSocialId(social_type, id);

        return user;
    }

    public User signUp(String social_type, String social_token, String nickname) throws ParseException {

        String id = null;

        if (social_type.equals("kakao")) {
            id = kakaoService.getUserInfo(social_token);
        } else if (social_type.equals("apple")) {
            id = appleService.userIdFromApple(social_token);
        }

        if (id == null) {
            throw new UnauthorizedException(ErrorResponseStatus.UNAUTHORIZED_INVALID_SOCIAL_TOKEN);
        }

        UserDto userDto = new UserDto();
        userDto.setSocialType(social_type);
        userDto.setSocialId(id);
        userDto.setNickname(nickname);
        userDto.setRoles(Collections.singletonList("ROLE_USER"));

        return userRepository.save(new User(userDto));
    }

    public void signOut(String token){
        if (!jwtTokenProvider.validateToken(token)){
            throw new UnauthorizedException(ErrorResponseStatus.NOT_FOUND_USER_EXCEPTION);
        } else {
            User user = findUserBySocialId(jwtTokenProvider.getUserSocialId(token));
            userRepository.delete(user);
        }
    }

    public void validateDuplicateNickname(String nickname){
        if(userRepository.existsByNickname(nickname))
            throw new ConflictException(ErrorResponseStatus.CONFLICT_DUPLICATED_NICKNAME);
    }

    private User findUserBySocialTypeAndSocialId(String socialType, String socialId) {
        return userRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .orElseThrow(() -> new NotFoundException(ErrorResponseStatus.NOT_FOUND_USER_EXCEPTION));
    }

    public User findUserBySocialId(String socialId) {
        return userRepository.findBySocialId(socialId).orElseGet(() -> null);
    }

    public HashMap<String, Integer> getUserRunningNumericData(User user){
        String userId = user.getId();
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime thisMonth =
                LocalDateTime.of(nowDateTime.getYear(), nowDateTime.getMonth(), 1, 0, 0,0);
        Integer lastRunning = userRunningRecordRepository.countAllByUserId(userId);
        Integer thisMonthRunning = userRunningRecordRepository.countAllThisMonthRunningByUserId(userId, thisMonth);
        Integer plannedRunning = userGatheringRelationRepository.countAllPlannedRunningByUserId(userId, nowDateTime);

        return new LinkedHashMap<>(){{
            put("last_running", lastRunning);
            put("this_month_running", thisMonthRunning);
            put("planned_running", plannedRunning);
        }};
    }

    public HashMap<String, Object> getUserRunningStats(User user){

        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        List<RunningRecordDto> runningRecords =
                userRunningRecordRepository.findWeekAgoRunningRecordDtoByUserId(user.getId(), weekAgo);

        Double valueCourseLengthWeek = 0.0;
        Double valueCourseLengthToday = 0.0;
        Double valueCalWeek = 0.0;
        Double valueCalToday = 0.0;

        for (RunningRecordDto runningRecord : runningRecords) {
            Double length = runningRecord.getCourse().getLength();
            Long hours = ChronoUnit.HOURS.between(runningRecord.getStartTime(), runningRecord.getEndTime());
            Double cal = calculateCalories(length, hours);
            valueCourseLengthWeek += length;
            valueCalWeek += cal;
            if(runningRecord.getStartTime().isAfter(LocalDate.now().atTime(0, 0))){
                valueCourseLengthToday += length;
                valueCalToday += cal;
            }
        }
        Double courseLengthWeek = Math.round(valueCourseLengthWeek * 100) / 100.0;
        Double courseLengthToday = Math.round(valueCourseLengthToday * 100) / 100.0;
        Double calWeek = Math.round(valueCalWeek * 100) / 100.0;
        Double calToday = Math.round(valueCalToday * 100) / 100.0;
        return new LinkedHashMap<>(){{
            put("km_week", courseLengthWeek);
            put("km_today", courseLengthToday);
            put("cal_week", calWeek);
            put("cal_today", calToday);
        }};
    }

    public List<GatheringRecordDto> getCloseRunningSchedule(User user){
        LocalDateTime todayMidNight = getTodayMidnightLocalDateTime();
        List<GatheringRecordDto> gatheringRecords = userRunningRecordRepository.findGatheringDtoByUserId(user.getId(), todayMidNight);
        for (GatheringRecordDto gatheringRecord : gatheringRecords) {
            gatheringRecord.setImageUrl(KeyService.addBaseURLToImage(gatheringRecord.getImageUrl()));
        }
        if(gatheringRecords.size() >= 3)
            return gatheringRecords.subList(0,3);
        else
            return gatheringRecords;
    }

    private Double calculateCalories(Double length, Long hours){
        Double kmPerHour = length/hours;
        Double mets;
        if(kmPerHour >= 10.8)
            mets = 10.5;
        else if(kmPerHour >= 8)
            mets = 8.3;
        else if(kmPerHour >= 7)
            mets = 6.0;
        else if(kmPerHour >= 6.4)
            mets = 5.0;
        else
            mets = 3.0;
        return 1.05 * mets * hours * 50;
    }

    public void updateProfile(User user, String nickname, MultipartFile image){

        imageUploadService.deleteImage(user.getImageUrl());

        if(!image.isEmpty())
            user.updateProfile(nickname, imageUploadService.restore(image, "user"));
        else
            user.updateProfile(nickname, null);

        userRepository.save(user);
    }

    public HashMap<String, Object> lookupReservedGathering(User user){
        LocalDateTime todayMidNight = getTodayMidnightLocalDateTime();
        List<GatheringLookUpDto> gatheringLookUps = gatheringRepository.searchGatheringLookUpDtoByUser(user.getId(), todayMidNight);
        List<GatheringLookUpDto> gatheringLookUpsToday = new ArrayList<>();
        List<GatheringLookUpDto> gatheringLookUpsFuture = new ArrayList<>();

        for (GatheringLookUpDto gatheringLookUp : gatheringLookUps) {
            if(gatheringLookUp.getDateTime().isBefore(todayMidNight.plusDays(1))){
                gatheringLookUpsToday.add(gatheringLookUp);
            }
            else{
                gatheringLookUpsFuture.add(gatheringLookUp);
            }
        }
        return new LinkedHashMap<>() {{
            put("today_gatherings", gatheringLookUpsToday);
            put("planned_gatherings", gatheringLookUpsFuture);
        }};
    }


    private LocalDateTime getTodayMidnightLocalDateTime(){
        LocalDateTime todayMidNight = LocalDateTime.now();
        todayMidNight = todayMidNight.of(todayMidNight.getYear(),todayMidNight.getMonth(),todayMidNight.getDayOfMonth(),00,00);
        return todayMidNight;
    }
}
