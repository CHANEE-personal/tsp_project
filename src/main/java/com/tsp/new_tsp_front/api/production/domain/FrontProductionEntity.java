package com.tsp.new_tsp_front.api.production.domain;

import com.tsp.new_tsp_front.api.common.domain.NewCommonMappedClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_production")
public class FrontProductionEntity extends NewCommonMappedClass {

	@Transient
	private Integer rnum;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx")
	private Integer idx;

	@Column(name = "title")
	@NotEmpty(message = "프로덕션 제목 입력은 필수입니다.")
	private String title;

	@Column(name = "description")
	@Lob
	@NotEmpty(message = "프로덕션 상세내용 입력은 필수입니다.")
	private String description;

	@Column(name = "visible")
	private String visible;
}
