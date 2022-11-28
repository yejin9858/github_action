package com.wayrunny.runway.service;

import com.wayrunny.runway.domain.dto.gathering.*;
import com.wayrunny.runway.domain.entity.*;
import com.wayrunny.runway.repository.*;
import com.wayrunny.runway.util.response.error.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    private final CourseRepository courseRepository;
    private final TagRepository tagRepository;
    private final UserGatheringRelationRepository userGatheringRelationRepository;
    private final UserRunningRecordRepository userRunningRecordRepository;

    public void createGathering(GatheringDto.GatheringCreateDto gatheringCreateDto, User user ) {
        String dateTime = gatheringCreateDto.getDate() + " " + gatheringCreateDto.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        GatheringDto gatheringDto = new GatheringDto(gatheringCreateDto.getName(),
                LocalDateTime.parse(dateTime, formatter),
                gatheringCreateDto.getContent(), gatheringCreateDto.getMaxPerson(),
                gatheringCreateDto.getSecret(), gatheringCreateDto.getPassword());

        Course course = courseRepository.findById(gatheringCreateDto.getCourseId()).orElseThrow(() -> new NotFoundException(ErrorResponseStatus.NOT_FOUND_COURSE_EXCEPTION));

        Gathering newGathering = new Gathering(gatheringDto, course, user);
        gatheringRepository.save(newGathering);

        userGatheringRelationRepository.save(new UserGatheringRelation(user.getId(), newGathering.getId()));

        return;
    }

    public Boolean checkGatheringPassword(GatheringPWDto requestGatheringPWDto){

        Gathering gathering = gatheringRepository.findById(requestGatheringPWDto.getGatheringId()).orElseThrow(()->new NotFoundException(ErrorResponseStatus.NOT_FOUND_GATHERING_EXCEPTION));

        GatheringPWDto gatheringPWDto = new GatheringPWDto(gathering);

        if (gatheringPWDto.getPassword().equals(requestGatheringPWDto.getPassword())) {
            return true;
        } else {
            throw new UnauthorizedException(ErrorResponseStatus.NOT_FOUND_PASSWORD_EXCEPTION);
        }

    }

    public List<GatheringListDto> getFastParticipatedGathering() {
        List<GatheringListDto> gatheringListDtoList = gatheringRepository.findFastParticipatedGathering();
        return gatheringListDtoList;
    }

    public List<GatheringListDto> getNewGatherings() {
        List<GatheringListDto> gatheringListDtoList = gatheringRepository.findAllNewGathering();
        return gatheringListDtoList;
    }

    private Double calculateDistance(Double x1, Double y1, Double x2, Double y2){
        return Math.sqrt(Math.pow(x1 - x2,2)+Math.pow(y1-y2, 2));
    }

    public GatheringDetailDto viewGatheringDetail(Long gatheringId){
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundException(ErrorResponseStatus.NOT_FOUND_GATHERING_EXCEPTION));
        List <String> tagNameList = tagRepository.findAllTagNameByCourseId(gathering.getCourse().getId());
        return new GatheringDetailDto(gathering, tagNameList);
    }

    public void joinGathering(String userId, Long gatheringId){
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                ()-> new NotFoundException(ErrorResponseStatus.NOT_FOUND_GATHERING_EXCEPTION)
        );
        if(!userRepository.existsById(userId))
            throw new NotFoundException(ErrorResponseStatus.NOT_FOUND_USER_EXCEPTION);
        if(userGatheringRelationRepository.existsByAndUserIdAndGatheringId(userId, gatheringId)
                || gathering.getParticipatedPerson() >= gathering.getMaxPerson())
            throw new ConflictException(ErrorResponseStatus.CONFLICT_CANNOT_PARTICIPATE);
        userGatheringRelationRepository.save(new UserGatheringRelation(userId, gatheringId));
        gathering.addParticipatedPerson(1);
        gatheringRepository.save(gathering);
    }

    public List<GatheringListDto> searchGatheringByName(String keyword){
        return gatheringRepository.searchGatheringListDtoByCourseName(keyword);
    }

    public String attendanceGathering(User user, UserRunningRecordDto recordDto){

        if(userRunningRecordRepository.existsByUserIdAndGatheringId(user.getId(), recordDto.getGatheringId())){
            throw new ConflictException(ErrorResponseStatus.CONFLICT_ALREADY_PARTICIPATED);
        }
        String runningKind = "";
        String startDateTime = recordDto.getStartDate() + " " + recordDto.getStartTime();
        String endDateTime = recordDto.getEndDate() + " " + recordDto.getEndTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endDateTime, formatter);
        Integer exp = user.getExp();
        Integer level = user.getLevel();

        UserRunningRecord runningRecord;
        if(recordDto.getGatheringId() != null) {
            Course course = courseRepository.findCourseByGatheringId(recordDto.getGatheringId())
                    .orElseThrow(() -> new NotFoundException(ErrorResponseStatus.NOT_FOUND_GATHERING_EXCEPTION));
            runningRecord = new UserRunningRecord(user.getId(), recordDto.getGatheringId(), course.getId(), startTime, endTime);
            runningKind = "번개방 참여 기록 저장";
            exp += 30;

        }
        else if(recordDto.getCourseId() != null){
            Course course = courseRepository.findById(recordDto.getCourseId())
                    .orElseThrow(() -> new NotFoundException(ErrorResponseStatus.NOT_FOUND_COURSE_EXCEPTION));
            runningRecord = new UserRunningRecord(user.getId(), recordDto.getGatheringId(), course.getId(), startTime, endTime);
            runningKind = "개인 러닝 참여 기록 저장";
            exp += 20;
        }
        else{
            throw new BadRequestException(ErrorResponseStatus.BAD_REQUEST_MISSING_COURSE_GATHERING_BOTH);
        }
        userRunningRecordRepository.save(runningRecord);

        //로직 변경
        if(exp>=300){
            exp -= 300;
            level ++;
        }
        user.setLevelAndExp(exp, level);
        userRepository.save(user);
        return runningKind;
    }

    public List<GatheringListDto> getNowGatherings(User user){
        LocalDateTime now = LocalDateTime.now();
        return gatheringRepository.findNearTimeGatheringListDto(now.minusHours(1), now.plusHours(1));
    }

}
