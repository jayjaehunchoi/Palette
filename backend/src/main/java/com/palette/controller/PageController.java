package com.palette.controller;

import com.palette.dto.GeneralResponse;
import com.palette.dto.SearchCondition;
import com.palette.service.PostGroupService;
import com.palette.service.PostService;
import com.palette.utils.constant.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/page")
@RequiredArgsConstructor
@RestController
public class PageController {

    private final PostService postService;
    private final PostGroupService postGroupService;

    @GetMapping("/postgroup")
    public ResponseEntity<GeneralResponse> getGroupPostCountWithFilter(@RequestParam(required = false, defaultValue = "none") String filter, @RequestParam(required = false) String condition, @RequestParam(defaultValue = "1", required = false) int page){
        long totalPage = postGroupService.getTotalPage(filter, condition);
        GeneralResponse<Object> res = GeneralResponse.builder().data(totalPage).build();
        return ResponseEntity.ok(res);
    }


    @GetMapping("/post")
    public ResponseEntity<GeneralResponse> getPostPage(@RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String region,
                                                       @RequestParam(required = false) String title,
                                                       @RequestParam(defaultValue = ConstantUtil.DEFAULT_PAGE,required = false) int page){
        SearchCondition searchCondition = SearchCondition.setSearchCondition(name, region, title);
        long totalPage = postService.getTotalPage(searchCondition);
        GeneralResponse<Object> res = GeneralResponse.builder().data(totalPage).build();
        return ResponseEntity.ok(res);
    }

}
