package com.palette.controller;

import com.palette.controller.util.RestDocUtil;
import com.palette.domain.group.Group;
import com.palette.domain.group.MemberGroup;
import com.palette.domain.member.Member;
import com.palette.dto.request.GroupDto;
import com.palette.dto.request.GroupJoinDto;
import com.palette.dto.request.GroupUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;


import java.nio.charset.StandardCharsets;

import static com.palette.utils.constant.SessionUtil.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
public class GroupControllerTest extends RestDocControllerTest{

    @Autowired GroupController groupController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider, groupController);

        Member member = createMember();

        session.setAttribute(MEMBER,member);
    }


    // todo : controller 확인 필요
    @Test
    void 여행_그룹_조회() throws Exception {
        Member member = (Member) session.getAttribute(MEMBER);
        Group group = createGroup();
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.addMemberGroup(group,member);

        restDocsMockMvc.perform(get("/travelgroup").session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("group_read_groups",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 단건_여행_그룹_조회() throws Exception{
        Member member = (Member) session.getAttribute(MEMBER);
        Group group = createGroup();
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.addMemberGroup(group,member);

        given(groupService.findById(anyLong())).willReturn(group);
        doNothing().when(groupService).isMemberHaveAuthToUpdate(any(),any());
        restDocsMockMvc.perform(get("/travelgroup/1").session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("group_read_single_group",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));

    }

    @Test
    void 그룹_생성() throws Exception{
        GroupDto groupDto = new GroupDto("groupName", "groupIntro");
        String json = objectMapper.writeValueAsString(groupDto);

        given(groupService.addGroup(any(),any())).willReturn(1L);

        restDocsMockMvc.perform(post("/travelgroup").session(session)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("group_create_group"));
    }

    @Test
    void 그룹_가입() throws Exception{
        doNothing().when(groupService).addGroupMember(any(),any());
        GroupJoinDto groupJoinDto = new GroupJoinDto(CODE);
        String json = objectMapper.writeValueAsString(groupJoinDto);

        restDocsMockMvc.perform(post("/travelgroup/join").session(session)
                .content(json)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("group_join_group"));
    }

    @Test
    void 그룹_탈퇴() throws Exception{
        doNothing().when(groupService).deleteGroup(anyLong());
        restDocsMockMvc.perform(delete("/travelgroup/1/member").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("group_delete_member"));
    }

    @Test
    void 그룹_수정() throws Exception{
        doNothing().when(groupService).updateGroup(anyLong(),any());
        GroupUpdateDto update = new GroupUpdateDto("groupName", "groupIntro");
        String json = objectMapper.writeValueAsString(update);
        restDocsMockMvc.perform(put("/travelgroup/1").session(session)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("group_update_member",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 그룹_삭제() throws Exception{
        doNothing().when(groupService).deleteGroup(anyLong());
        restDocsMockMvc.perform(delete("/travelgroup/1").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("group_delete_group"));
    }

    private Member createMember(){
        return new Member(NAME, PASSWORD,IMAGE,EMAIL);
    }

    private Group createGroup() {
        return Group.builder().groupName("groupName").groupsIntroduction("This is Group").build();
    }

}
