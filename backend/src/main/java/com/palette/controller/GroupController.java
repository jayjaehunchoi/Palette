package com.palette.controller;

import com.palette.domain.group.Budget;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.GroupDto;
import com.palette.dto.request.GroupJoinDto;
import com.palette.dto.request.GroupUpdateDto;
import com.palette.dto.response.GroupResponseDto;
import com.palette.dto.response.GroupsResponseDto;
import com.palette.service.BudgetService;
import com.palette.service.GroupService;
import com.palette.controller.auth.AuthenticationPrincipal;
import com.palette.utils.annotation.LoginChecker;
import com.palette.utils.constant.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/api/travelgroup")
@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;
    private final BudgetService budgetService;

    //전체 그룹 조회( plan 버튼 클릭 했을 때)
    @LoginChecker
    @GetMapping
    public ResponseEntity<GroupsResponseDto> readGroups(@AuthenticationPrincipal Member member){
        //나의 모든 그룹들 response 해주기
        List<Group> groups = getGroups(member);
        GroupsResponseDto dto = new GroupsResponseDto();
        dto.setResponseDtoGroups(groups);
        return ResponseEntity.ok(dto);
    }


    //단건 그룹 조회 ( 해당 그룹 클릭 했을 때 )
    @LoginChecker
    @GetMapping("/{travelgroupid}")
    public ResponseEntity<GroupResponseDto> readGroup(@AuthenticationPrincipal Member member, @PathVariable("travelgroupid") Long travelGroupId){
        Group group = groupService.findById(travelGroupId);
        groupService.isMemberHaveAuthToUpdate(member,group);
        GroupResponseDto dto = createSingleGroupDto(group);
        List<Member> members = getMembers(group);
        dto.setMembers(members);
        return ResponseEntity.ok(dto);
    }

    //그룹 생성( 그룹생성-> 확인 버튼 눌렀을 때 )
    @PostMapping
    public Long addGroup(@AuthenticationPrincipal Member member, @RequestBody @Validated GroupDto groupDto){
        Group group = new Group(groupDto.getGroupName(),groupDto.getGroupIntroduction());
        groupService.addGroup(group,member);
        budgetService.addBudget(member,group,new Budget(group,0L));
        return groupService.addGroup(group,member);
    }

    //그룹 가입( 그룹 들어가기-> 가입 버튼 눌렀을 때)
    @LoginChecker
    @PostMapping("/join") //test완료 : 이미 가입된 그룹일때도 테스트완료
    public ResponseEntity<Void> joinGroup(@AuthenticationPrincipal Member member, @RequestBody @Validated GroupJoinDto groupJoinDto){
        String groupCode = groupJoinDto.getCode();
        groupService.addGroupMember(groupCode,member);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //그룹 탈퇴
    @LoginChecker
    @DeleteMapping("{travelgroupid}/member") //test 완료
    public ResponseEntity<Void> ExitGroup(@AuthenticationPrincipal Member member, @PathVariable("travelgroupid") Long id){
        groupService.deleteGroupMember(id,member);
        return HttpResponseUtil.RESPONSE_OK;
    }

    //그룹 수정
    @LoginChecker
    @PutMapping("/{travelgroupid}") //test 완료
    public ResponseEntity<GroupUpdateDto> updateGroup(@AuthenticationPrincipal Member member, @PathVariable("travelgroupid") Long id , @RequestBody @Validated GroupUpdateDto groupUpdateDto){
        groupService.updateGroup(id,groupUpdateDto);
        return ResponseEntity.ok(groupUpdateDto);
    }

    //그룹 삭제
    @LoginChecker
    @DeleteMapping("/{travelgroupid}")
    public ResponseEntity<Void> deleteGroup(@AuthenticationPrincipal Member member, @PathVariable("travelgroupid") Long id){
        groupService.deleteGroup(id);
        return HttpResponseUtil.RESPONSE_OK;
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

    private List<Group> getGroups(Member member) {
        List<Group> groups = new ArrayList<>();
        List<MemberGroup> memberGroups = member.getMemberGroups();
        for(int i = 0; i < memberGroups.size(); i++){
            groups.add(memberGroups.get(i).getGroup());
        }
        return groups;
    }

    //group을 groupResponseDto로 변환
    private GroupResponseDto createSingleGroupDto(Group group){
        return new GroupResponseDto(group);
    }
}
