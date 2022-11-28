package com.wayrunny.runway.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/* 생성 일자, 수정 일자를 담은 추상 클래스 */

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class Timestamped {

    /* 생성 일자 */
    @CreatedDate
    private LocalDateTime createdAt;

    /* 수정 일자 */
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
