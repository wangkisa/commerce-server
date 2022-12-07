package com.wangkisa.commerce.domain.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @CreatedDate
    @Column(name = "created_at", columnDefinition = "timestamp with time zone", updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
    private LocalDateTime updatedAt;
}
