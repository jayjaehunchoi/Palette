package com.palette.repository;

import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberGroupRepository extends JpaRepository<MemberGroup,Long> {
    public MemberGroup findByMemberAndGroup(Member member, Group group);
}
