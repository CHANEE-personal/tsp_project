package com.tsp.new_tsp_front.common;

import com.tsp.new_tsp_front.common.paging.Page;
import com.tsp.new_tsp_front.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SearchCommon {

	/**
	 * <pre>
	 * 1. MethodName : searchCommon
	 * 2. ClassName  : SearchCommon.java
	 * 3. Comment    : 페이징 및 검색 조건 공통
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 08. 08.
	 * </pre>
	 *
	 * @param  page
	 * @param  paramMap
	 * @return ConcurrentHashMap
	 * @throws Exception
	 */
	public ConcurrentHashMap<String, Object> searchCommon(Page page, @RequestParam(required = false) Map<String, Object> paramMap) {

		ConcurrentHashMap<String, Object> searchMap = new ConcurrentHashMap<>();

		// 페이징 처리
		Integer pageCnt = StringUtil.getInt(page.getPage(), 1);
		Integer pageSize = StringUtil.getInt(page.getSize(), 10);
		page.setPage(pageCnt);
		page.setSize(pageSize);

		// 검색 조건
		searchMap.put("searchType", StringUtil.getString(paramMap.get("searchType"), ""));
		searchMap.put("searchKeyword", StringUtil.getString(paramMap.get("searchKeyword"), ""));
		searchMap.put("jpaStartPage", StringUtil.getInt(page.getStartPage(), 0));
		searchMap.put("startPage", pageCnt);
		searchMap.put("size", pageSize);

		return searchMap;
	}
}
