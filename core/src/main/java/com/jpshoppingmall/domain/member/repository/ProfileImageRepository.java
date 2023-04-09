package com.jpshoppingmall.domain.member.repository;

import com.jpshoppingmall.domain.member.entity.ProfileImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByMemberId(Long memberId);
}
