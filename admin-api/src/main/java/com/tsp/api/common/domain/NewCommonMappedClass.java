package com.tsp.api.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class NewCommonMappedClass {

    @CreatedBy
    @Column(name = "creator", updatable = false)
    @ApiModelProperty(required = true, value = "등록자")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updater")
    @ApiModelProperty(required = true, value = "수정자")
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(required = true, value = "등록 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_time")
    @ApiModelProperty(required = true, value = "수정 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;
}
