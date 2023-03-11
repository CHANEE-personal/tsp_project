package com.tsp.api.user.domain;

import com.tsp.api.common.domain.NewCommonMappedClass;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tsp_admin")
public class AdminUserEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "user_id", unique = true)
    @NotEmpty(message = "유저 ID 입력은 필수입니다.")
    private String userId;

    @Column(name = "password")
    @NotEmpty(message = "유저 Password 입력은 필수입니다.")
    private String password;

    @Column(name = "name")
    @NotEmpty(message = "유저 이름 입력은 필수입니다.")
    private String name;

    @Column(name = "email", unique = true)
    @Email
    @NotEmpty(message = "유저 이메일 입력은 필수입니다.")
    private String email;

    @Column(name = "visible")
    @NotEmpty(message = "유저 사용 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "user_token")
    private String userToken;

    @Column(name = "user_refresh_token")
    private String userRefreshToken;

    @Enumerated(value = STRING)
    private Role role;

    public void update(AdminUserEntity adminUserEntity) {
        this.userId = adminUserEntity.userId;
        this.name = adminUserEntity.name;
        this.email = adminUserEntity.email;
        this.visible = adminUserEntity.visible;
        this.role = adminUserEntity.role;
    }

    public void updateToken(String token) {
        this.userToken = token;
    }

    public void updateRefreshToken(String refreshToken) {
        this.userRefreshToken = refreshToken;
    }

    public static AdminUserDto toDto(AdminUserEntity entity) {
        if (entity == null) return null;
        return AdminUserDto.builder()
                .idx(entity.idx)
                .userId(entity.userId)
                .password(entity.password)
                .name(entity.name)
                .email(entity.email)
                .visible(entity.visible)
                .userToken(entity.userToken)
                .userRefreshToken(entity.userRefreshToken)
                .role(entity.role)
                .build();
    }

    public static List<AdminUserDto> toDtoList(List<AdminUserEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminUserEntity::toDto)
                .collect(Collectors.toList());
    }
}
