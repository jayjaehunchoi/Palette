package com.palette.controller;

import com.palette.dto.GeneralResponse;
import com.palette.dto.SearchCondition;
import com.palette.service.PostGroupService;
import com.palette.service.PostService;
import com.palette.utils.constant.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/page")
@RequiredArgsConstructor
@RestController
public class PageController {

    private final PostService postService;
    private final PostGroupService postGroupService;

    @GetMapping("/postgroup")
    public ResponseEntity<GeneralResponse> getGroupPostPageWithFilter(@ModelAttribute SearchCondition searchCondition, @RequestParam(defaultValue = "1", required = false) int page){
        long totalPage = postGroupService.getTotalPage(searchCondition);
        GeneralResponse<Object> res = GeneralResponse.builder().data(totalPage).build();
        return ResponseEntity.ok(res);
    }


    @GetMapping("/post")
    public ResponseEntity<GeneralResponse> getPostPage(@ModelAttribute SearchCondition searchCondition, @RequestParam(defaultValue = ConstantUtil.DEFAULT_PAGE,required = false) int page){
        long totalPage = postService.getTotalPage(searchCondition);
        GeneralResponse<Object> res = GeneralResponse.builder().data(totalPage).build();
        return ResponseEntity.ok(res);
    }

}
