package com.jpshoppingmall.domain.member.repository;

import com.jpshoppingmall.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findByEmail(String email);
    Boolean existsByNickname(String nickname);
    Boolean existsByEmail(String email);
}
