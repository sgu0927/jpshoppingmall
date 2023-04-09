package com.jpshoppingmall.domain.member.service;

import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.entity.ProfileImage;
import com.jpshoppingmall.domain.member.repository.ProfileImageRepository;
import com.jpshoppingmall.util.FileStore;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileImageWriteService {

    private final FileStore fileStore;
    // private final ProfileImageJdbcRepository profileImageJdbcRepository;
    private final ProfileImageRepository profileImageRepository;

    @Transactional
    public ProfileImage upload(Member member, MultipartFile file)
        throws IOException {
        ProfileImage profileImage = fileStore.storeFile(member, file);
        var savedProfileImage = profileImageRepository.save(profileImage);
        member.changeProfileImage(savedProfileImage);

        return savedProfileImage;
    }

    @Transactional
    public ProfileImage update(Member member, MultipartFile file) throws IOException {
        Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByMemberId(
            member.getId());
        if (optionalProfileImage.isPresent()) {
            ProfileImage profileImage = fileStore.updateFile(optionalProfileImage.get(), file);
            var savedProfileImage = profileImageRepository.save(profileImage);
            member.changeProfileImage(savedProfileImage);
            return savedProfileImage;
        }

        log.info("ProfileImage doesn't exists! - memberId :: {}", member.getId());

        return null;
    }
}
