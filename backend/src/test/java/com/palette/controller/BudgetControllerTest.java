package com.palette.controller;

import com.palette.controller.util.RestDocUtil;
import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.dto.request.BudgetDto;
import com.palette.dto.request.BudgetUpdateDto;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.BudgetResponseDto;
import com.palette.dto.response.ExpenseResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureRestDocs
public class BudgetControllerTest extends RestDocControllerTest{

    @Autowired BudgetController budgetController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider, budgetController);
    }

    @Test
    void 예산_조회() throws Exception{
        ExpenseResponseDto expenseDto = new ExpenseResponseDto(new Expense(Expense.Category.TRANSPORTATION, "내용", 1000L));
        BudgetResponseDto budgetResponseDto = new BudgetResponseDto(1L, 10000, 1000, 9000, Arrays.asList(expenseDto));
        given(budgetService.readBudget(any(),any())).willReturn(budgetResponseDto);

        restDocsMockMvc.perform(get("/api/travelgroup/1/budget").header(AUTH, TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("budget_get_budget",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));
    }

    @Test
    void 예산_저장() throws Exception{
        Group group = createGroup();
        given(groupService.findById(any())).willReturn(group);
        BudgetDto budgetDto = new BudgetDto(100000);
        String json = objectMapper.writeValueAsString(budgetDto);
        given(budgetService.addBudget(any(),any(),any())).willReturn(1L);

        restDocsMockMvc.perform(post("/api/travelgroup/1/budget").header(AUTH, TOKEN)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andDo(document("budget_create_budget"));
    }

    @Test
    void 예산_업데이트() throws Exception{
        Group group = createGroup();
        given(groupService.findById(any())).willReturn(group);
        Budget budget = new Budget(group, 10000L);
        given(budgetService.updateBudget(any(),any())).willReturn(budget);

        BudgetUpdateDto budgetUpdateDto = new BudgetUpdateDto(10000L);
        String json = objectMapper.writeValueAsString(budgetUpdateDto);

        budget.saveBudgetOnGroup(group);

        restDocsMockMvc.perform(put("/api/travelgroup/1/budget").header(AUTH, TOKEN)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(document("budget_update_budget"));
    }

    @Test
    void 예산_삭제() throws Exception{
        Group group = createGroup();
        given(groupService.findById(any())).willReturn(group);
        doNothing().when(budgetService).deleteBudget(any());

        restDocsMockMvc.perform(delete("/api/travelgroup/1/budget").header(AUTH, TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("budget_delete_budget"));

    }

}
