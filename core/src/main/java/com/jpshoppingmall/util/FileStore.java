package com.jpshoppingmall.util;

import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.entity.ProfileImage;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.entity.ProductImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + '\\' + filename;
    }

    public ProfileImage storeFile(Member member, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return ProfileImage.builder()
            .member(member)
            .originalFileName(originalFilename)
            .storeFileName(storeFileName)
            .build();
    }

    public ProfileImage updateFile(ProfileImage originalImage, MultipartFile multipartFile)
        throws IOException {
        if (multipartFile.isEmpty()) {
            log.info("MultipartFile is empty!");
            return null;
        }

        File file = new File(getFullPath(originalImage.getStoreFileName()));
        if (file.exists()) { // 파일이 존재하면
            boolean hasDeleted = file.delete(); // 파일 삭제
            if (hasDeleted) {
                log.info("Profile Image Delete Success! path ::  {}", file.getPath());
            } else {
                log.info("Profile Image Delete Failed! path ::  {}", file.getPath());
            }
        } else {
            log.info("Profile Image doesn't exists. path :: {}", file.getPath());
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return ProfileImage.builder()
            .id(originalImage.getId())
            .member(originalImage.getMember())
            .originalFileName(originalFilename)
            .storeFileName(storeFileName)
            .build();
    }

    public ProductImage storeFile(Product product, MultipartFile multipartFile, Boolean isTitle)
        throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return ProductImage.builder()
            .product(product)
            .originalFileName(originalFilename)
            .storeFileName(storeFileName)
            .isTitle(isTitle)
            .build();
    }

    public ProductImage updateFile(ProductImage originalImage, MultipartFile multipartFile,
        Boolean isTitle)
        throws IOException {
        if (multipartFile.isEmpty()) {
            log.info("MultipartFile is empty!");
            return null;
        }

        File file = new File(getFullPath(originalImage.getStoreFileName()));
        if (file.exists()) { // 파일이 존재하면
            boolean hasDeleted = file.delete(); // 파일 삭제
            if (hasDeleted) {
                log.info("Product Image Delete Success! path ::  {}", file.getPath());
            } else {
                log.info("Product Image Delete Failed! path ::  {}", file.getPath());
            }
        } else {
            log.info("Product Image doesn't exists. path :: {}", file.getPath());
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return ProductImage.builder()
            .id(originalImage.getId())
            .product(originalImage.getProduct())
            .originalFileName(originalFilename)
            .storeFileName(storeFileName)
            .isTitle(isTitle)
            .build();
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
