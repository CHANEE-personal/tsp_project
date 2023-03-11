package com.tsp.api.common;

import com.tsp.api.common.image.AdminCommonImageJpaRepository;
import com.tsp.api.common.domain.CommonImageDto;
import com.tsp.api.common.domain.CommonImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tsp.api.common.EntityType.MODEL;
import static com.tsp.api.common.EntityType.PRODUCTION;

@Component
@RequiredArgsConstructor
public class SaveImage {
    private final EntityManager em;
    private final AdminCommonImageJpaRepository adminCommonImageJpaRepository;

    @Value("${image.uploadPath}")
    private String fileDirPath;

    public List<CommonImageDto> saveFile(List<MultipartFile> multipartFiles, CommonImageEntity commonImageEntity) throws IOException {
        if (adminCommonImageJpaRepository.findById(commonImageEntity.getIdx()).isPresent()) {
            adminCommonImageJpaRepository.delete(commonImageEntity);
        }
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        int index = 0;
        for(MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                commonImageEntityList.add(saveFile(multipartFile, commonImageEntity.getTypeName(), commonImageEntity.getTypeIdx(), index));
            }
            index++;
        }
        return CommonImageEntity.toDtoList(commonImageEntityList);
    }

    /**
     * <pre>
     * 1. MethodName : saveFile
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 단일 이미지 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public CommonImageEntity saveFile(MultipartFile multipartFile, EntityType entityType, Long idx, int index) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String fileId = createSaveFileName(multipartFile.getOriginalFilename());
        long fileSize = multipartFile.getSize();
        String mainOrSub = index == 0 ? "main" : "sub" + index;

        // 파일 업로드
        multipartFile.transferTo(new File(saveFilePath(fileId, entityType)));

        CommonImageEntity imageEntity = CommonImageEntity.builder()
                .filePath(saveFilePath(fileId, entityType))
                .fileName(multipartFile.getOriginalFilename())
                .fileSize(fileSize)
                .fileMask(fileId)
                .fileNum(index)
                .typeName(entityType)
                .typeIdx(idx)
                .imageType(mainOrSub)
                .visible("Y")
                .regDate(LocalDateTime.now())
                .build();

        em.persist(imageEntity);

        return imageEntity;
    }

    /**
     * <pre>
     * 1. MethodName : saveFilePath
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 이미지 업로드 경로
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public String saveFilePath(String saveFileName, EntityType entityType) {
        String typePath = (entityType == MODEL) ? "/model/" : (entityType == PRODUCTION) ? "/production/" : "/portfolio/";
        File dir = new File(fileDirPath+typePath);
        if (!dir.exists()) dir.mkdirs();
        return fileDirPath + typePath + saveFileName;
    }

    /**
     * <pre>
     * 1. MethodName : createSaveFileName
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 업로드 이미지 파일명 생성
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    private String createSaveFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);

        return uuid + ext;
    }

    /**
     * <pre>
     * 1. MethodName : extractExt
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 이미지 확장자 추출
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    private String extractExt(String originalFileName) {
        int idx = originalFileName.lastIndexOf(".");
        return originalFileName.substring(idx);
    }
}
