package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.member.service.MemberReadService;
import com.jpshoppingmall.domain.member.service.ProfileImageWriteService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UploadMemberImageUsecase {

    private final MemberReadService memberReadService;
    private final ProfileImageWriteService profileImageWriteService;

    @Transactional
    public void execute(Long memberId, MultipartFile image, Boolean isUpdate) throws IOException {
        var member = memberReadService.getMember(memberId);
        if(isUpdate) {
            profileImageWriteService.update(member, image);
        } else {
            profileImageWriteService.upload(member, image);
        }
    }
}
