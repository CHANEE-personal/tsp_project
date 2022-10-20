package com.tsp.new_tsp_front.api.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class NewCommonMappedClass {
    @Column(name = "creator", updatable = false)
    @ApiModelProperty(required = true, value = "등록자")
    private Long creator;

    @Column(name = "updater", insertable = false)
    @ApiModelProperty(required = true, value = "수정자")
    private Long updater;

    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(required = true, value = "등록 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    @Column(name = "update_time", insertable = false)
    @ApiModelProperty(required = true, value = "수정 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
}
