package com.palette.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.service.*;
import com.palette.utils.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected GroupService groupService;

    @MockBean
    protected LikeService likeService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected PostGroupService postGroupService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected S3Uploader s3Uploader;

    @MockBean
    protected SendEmailService sendEmailService;

    @MockBean
    protected BudgetService budgetService;

    @MockBean
    protected ExpenseService expenseService;
}
