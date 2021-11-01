package com.palette;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import com.palette.domain.member.Member;
import com.palette.domain.member.Profile;
import com.palette.persistence.MemberRepository;
import com.palette.persistence.ProfileRepository;

import lombok.extern.java.Log;


@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit

public class ProfileTests {
    @Autowired
    MemberRepository memberRepo;

    @Autowired
    ProfileRepository profileRepo;

    @Test
    public void testInsertMembers() { //더미 회원 생성

        IntStream.range(1, 101).forEach(i -> {
            Member member = new Member();
            member.setUid("user" + i);
            member.setUpw("pw" + i);
            member.setUname("사용자" + i);

            memberRepo.save(member);

        });

    }// end method

    @Test
    public void testInsertProfile() {

        Member member = new Member();
        member.setUid("user1");

        for (int i = 1; i < 5; i++) {

            Profile profile1 = new Profile();
            profile1.setFname("face" + i + ".jpg");

            if (i == 1) {
                profile1.setCurrent(true);
            }

            profile1.setMember(member);

            profileRepo.save(profile1);

        }
    }// end method
    // 수정, 삭제, 조회 작업

}
