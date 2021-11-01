package com.palette;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.palette.domain.member.Member;
import com.palette.repository.MemberRepository;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ProfileTests {
    @Autowired
    MemberRepository memberRepo;

    @Test
    public void testInsertMembers() { //더미 회원 생성

        IntStream.range(1, 101).forEach(i -> {
            Member member = new Member("pw" + i, "uname" + i, "profileFileName" + i + ".png");
            memberRepo.save(member);
        });

        List<Member> all = memberRepo.findAll();
        assertThat(all.size()).isEqualTo(100);
        assertThat(all.get(0).getProfileFileName()).isEqualTo("profileFileName" + 1 + ".png");
    }// end method
}
