package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.portfolio.domain.QFrontPortFolioEntity.frontPortFolioEntity;

@Repository
@RequiredArgsConstructor
public class FrontPortFolioJpaRepository {

	private final JPAQueryFactory queryFactory;

	private BooleanExpression searchPortFolio(Map<String, Object> modelMap) {
		String searchType = StringUtil.getString(modelMap.get("searchType"),"");
		String searchKeyword = StringUtil.getString(modelMap.get("searchKeyword"),"");

		if ("0".equals(searchType)) {
			return frontPortFolioEntity.title.contains(searchKeyword)
					.or(frontPortFolioEntity.description.contains(searchKeyword));
		} else if ("1".equals(searchType)) {
			return frontPortFolioEntity.title.contains(searchKeyword);
		} else {
			return frontPortFolioEntity.description.contains(searchKeyword);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getPortFolioListCnt
	 * 2. ClassName  : FrontPortFolioJpaRepository.java
	 * 3. Comment    : 포트폴리오 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 11.
	 * </pre>
	 *
	 * @param portFolioMap
	 * @throws Exception
	 */
	public Long getPortFolioListCnt(Map<String, Object> portFolioMap) throws Exception {

		return queryFactory.selectFrom(frontPortFolioEntity)
				.where(searchPortFolio(portFolioMap))
				.fetchCount();
	}

	/**
	 * <pre>
	 * 1. MethodName : getPortFolioList
	 * 2. ClassName  : FrontPortFolioList.java
	 * 3. Comment    : 포트폴리오 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 11.
	 * </pre>
	 *
	 * @param portFolioMap
	 * @throws Exception
	 */
	public List<FrontPortFolioDTO> getPortFolioList(Map<String, Object> portFolioMap) throws Exception {

		List<FrontPortFolioEntity> portFolioList = queryFactory
				.selectFrom(frontPortFolioEntity)
				.where(searchPortFolio(portFolioMap))
				.orderBy(frontPortFolioEntity.idx.desc())
				.offset(StringUtil.getInt(portFolioMap.get("jpaStartPage"),0))
				.limit(StringUtil.getInt(portFolioMap.get("size"),0))
				.fetch();

		List<FrontPortFolioDTO> portFolioDtoList = PortFolioMapper.INSTANCE.toDtoList(portFolioList);

		for(int i = 0; i < portFolioDtoList.size(); i++) {
			portFolioDtoList.get(i).setRnum(StringUtil.getInt(portFolioMap.get("startPage"),1)*(StringUtil.getInt(portFolioMap.get("size"),1))-(2-i));
		}

		return portFolioDtoList;
	}
}
