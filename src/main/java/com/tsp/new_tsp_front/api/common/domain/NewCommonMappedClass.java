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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class NewCommonMappedClass {

	@Column(name = "creator", updatable = false)
	@ApiModelProperty(required = true, value = "등록자")
	private Integer creator;

	@Column(name = "updater", insertable = false)
	@ApiModelProperty(required = true, value = "수정자")
	private Integer updater;

	@Column(name = "create_time", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@ApiModelProperty(required = true, value = "등록 일자")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createTime;

	@Column(name = "update_time", insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@ApiModelProperty(required = true, value = "수정 일자")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateTime;
}
