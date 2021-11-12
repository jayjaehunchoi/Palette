package com.palette.controller;

import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.dto.request.GroupDto;
import com.palette.dto.request.GroupJoinDto;
import com.palette.dto.response.GroupResponseDto;
import com.palette.service.GroupService;
import com.palette.utils.annotation.Login;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/travelgroup")
@RequiredArgsConstructor
@Controller
public class GroupController {

    private GroupService groupService;

    //그룹 생성, 그룹 조회, 그룹 수정(삭제), 그룹 입장, 그룹 코드 조회(그룹 마이파이지 조회)

    //그룹 조회
    @GetMapping("/{travelgroupid}")
    public ResponseEntity<GroupResponseDto>  readGroup(@Login Member member, @PathVariable("travelgroupid") long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        GroupResponseDto dto = createSingleGroupDto(group);

        return ResponseEntity.ok(dto);
    }

    //그룹 생성
    @PostMapping
    public void addGroup(@Login Member member, @RequestBody @Validated GroupDto groupDto){
        Group group = new Group(groupDto.getGroupName(),groupDto.getGroupIntroduction());
        groupService.addGroup(group,member);
    }

    //그룹 입장 , 그룹코드로 바로 입장 or 그룹 리스트에 넣어주기만할까? 고민해보장
    @PostMapping("") // todo: url 정하기
    public void joinGroup(@Login Member member,@RequestBody @Validated GroupJoinDto groupJoinDto){
        long groupCode = groupJoinDto.getCode();
        groupService.addGroupMember(groupCode,member);
    }

    //그룹 수정
    @PutMapping("/{travelgroupid}")
    public void updateGroup(){

    }

    //그룹 삭제
    @DeleteMapping("/{travelgroupid}")
    public void deleteGroup(){

    }

    //그룹 마이페이지 조회
    @GetMapping("{travelgroupid}/my")
    public void readGroupMyPage(){

    }

    private GroupResponseDto createSingleGroupDto(Group group){
        return new GroupResponseDto(group);
    }
}
