package com.palette.domain.post;

import com.palette.domain.Period;
import com.palette.domain.member.Member;
import com.palette.dto.request.PostGroupDto;
import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PostGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_post_group_member"))
    private Member member;

    private String title;

    @Embedded
    private MyFile thumbNail;

    @Embedded
    private Period period;

    private String region;

    @Builder
    public PostGroup(Member member, String title, Period period, String region) {
        this.member = member;
        this.title = title;
        this.period = period;
        this.region = region;
    }

    public void setThumbNail(MyFile file){
        thumbNail = file;
    }

    public void update(PostGroupDto dto){
        title = dto.getTitle();
        period = new Period(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        region = dto.getRegion();
    }
}
