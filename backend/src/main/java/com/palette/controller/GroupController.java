package com.palette.controller;

import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.GroupDto;
import com.palette.dto.request.GroupJoinDto;
import com.palette.dto.request.GroupUpdateDto;
import com.palette.dto.response.GroupResponseDto;
import com.palette.dto.response.GroupsResponseDto;
import com.palette.service.GroupService;
import com.palette.utils.annotation.Login;
import com.palette.utils.annotation.LoginChecker;
import com.palette.utils.constant.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/travelgroup")
@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;

    //전체 그룹 조회( plan 버튼 클릭 했을 때)
    @LoginChecker
    @GetMapping //테스트 완료
    public ResponseEntity<GroupsResponseDto> readGroups(@Login Member member){
        //나의 모든 그룹들 response 해주기
        List<Group> groups = getGroups(member);
        GroupsResponseDto dto = new GroupsResponseDto();
        dto.setResponseDtoGroups(groups);
        return ResponseEntity.ok(dto);
    }

    private List<Group> getGroups(Member member) {
        List<Group> groups = new ArrayList<>();
        List<MemberGroup> memberGroups = member.getMemberGroups();
        for(int i = 0; i < memberGroups.size(); i++){
            groups.add(memberGroups.get(i).getGroup());
        }
        return groups;
    }

    //단건 그룹 조회 ( 해당 그룹 클릭 했을 때 )
    @LoginChecker
    @GetMapping(value = "/{travelgroupid}",consumes = MediaType.APPLICATION_JSON_VALUE) //test완료
    public ResponseEntity<GroupResponseDto> readGroup(@Login Member member, @PathVariable("travelgroupid") Long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        groupService.isMemberHaveAuthToUpdate(member,group);
        GroupResponseDto dto = createSingleGroupDto(group);
        List<Member> members = getMembers(group);
        dto.setMembers(members);
        return ResponseEntity.ok(dto);
    }

    //memberGroups에서 각각 멤버들 가져와서 member 리스트에 넣기
    private List<Member> getMembers(Group group) {
        List<Member> members = new ArrayList<>();
        List<MemberGroup> memberGroups = group.getMemberGroups();
        for(int i = 0 ; i < memberGroups.size(); i++){
            members.add(memberGroups.get(i).getMember());
        }
        return members;
    }

    //그룹 생성( 그룹생성-> 확인 버튼 눌렀을 때 )
    @PostMapping //test완료, groupId 값 넣지말기
    public Long addGroup(@Login Member member, @RequestBody @Validated GroupDto groupDto){
        Group group = new Group(groupDto.getGroupName(),groupDto.getGroupIntroduction());
        return groupService.addGroup(group,member);
    }

    //그룹 가입( 그룹 들어가기-> 가입 버튼 눌렀을 때)
    @LoginChecker
    @PostMapping("/{travelgroupid}") //test완료 : 이미 가입된 그룹일때도 테스트완료
    public ResponseEntity<Void> joinGroup(@Login Member member,@RequestBody @Validated GroupJoinDto groupJoinDto){
        String groupCode = groupJoinDto.getCode();
        groupService.addGroupMember(groupCode,member);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //그룹 탈퇴
    @LoginChecker
    @DeleteMapping("{travlegroupid}/member") //test 완료
    public ResponseEntity<Void> ExitGroup(@Login Member member,@RequestBody GroupDto groupDto){
        groupService.deleteGroupMember(groupDto.getGroupId(),member);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //그룹 수정
    @LoginChecker
    @PutMapping("/{travelgroupid}") //test 완료
    public ResponseEntity<GroupUpdateDto> updateGroup(@Login Member member, @RequestBody GroupUpdateDto groupUpdateDto){
        groupService.updateGroup(groupUpdateDto.getGroupId(),groupUpdateDto);
        return ResponseEntity.ok(groupUpdateDto);
    }

    //그룹 삭제
    @LoginChecker
    @DeleteMapping("/{travelgroupid}")
    public ResponseEntity<Void> deleteGroup(@Login Member member,@RequestBody GroupDto groupDto){
        groupService.deleteGroup(groupDto.getGroupId());
        return HttpResponseUtil.RESPONSE_OK;
    }

    //group을 groupResponseDto로 변환
    private GroupResponseDto createSingleGroupDto(Group group){
        return new GroupResponseDto(group);
    }
}