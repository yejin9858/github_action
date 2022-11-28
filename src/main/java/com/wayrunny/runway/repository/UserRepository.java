package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findBySocialTypeAndSocialId(String socialType, String socialId);
    //token, type 둘다 충족하는 회원을 조회하는 메소드 추가

    Optional<User> findBySocialId(String socialId);

    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    boolean existsBySocialId(String socialId);


}