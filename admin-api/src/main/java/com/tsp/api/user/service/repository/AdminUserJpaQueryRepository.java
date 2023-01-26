package com.tsp.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.user.domain.AdminUserDTO;
import com.tsp.api.user.domain.AdminUserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.api.domain.user.QAdminUserEntity.adminUserEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminUserJpaQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 유저 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Page<AdminUserDTO> findUserList(Map<String, Object> userMap, PageRequest pageRequest) {
        List<AdminUserEntity> userList = queryFactory.selectFrom(adminUserEntity)
                .where(adminUserEntity.visible.eq("Y"))
                .orderBy(adminUserEntity.idx.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(AdminUserEntity.toDtoList(userList), pageRequest, userList.size());
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
//    public String adminLogin(AdminUserEntity existAdminUserEntity) {
//        final String db_pw = nullStrToStr(findOneUser(existAdminUserEntity.getUserId()).getPassword());
//        String result;
//
//        if (passwordEncoder.matches(existAdminUserEntity.getPassword(), db_pw)) {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(existAdminUserEntity.getUserId(), existAdminUserEntity.getPassword())
//            );
//            getContext().setAuthentication(authentication);
//            result = "Y";
//        } else {
//            result = "N";
//        }
//        return result;
//    }
}
