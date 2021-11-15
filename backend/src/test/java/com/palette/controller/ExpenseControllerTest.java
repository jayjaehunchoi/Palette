package com.palette.controller;

import com.palette.controller.util.RestDocUtil;
import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.domain.member.Member;
import com.palette.dto.request.ExpenseDto;
import com.palette.dto.response.BudgetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.palette.utils.constant.SessionUtil.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
public class ExpenseControllerTest extends RestDocControllerTest{

    @Autowired ExpenseController expenseController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider, expenseController);

        Member member = createMember();

        session.setAttribute(MEMBER,member);
    }

    @Test
    void 지출_전체_조회() throws Exception{
        ExpenseDto expenseDto = new ExpenseDto(new Expense(Expense.Category.TRANSPORTATION, "내용", 1000L));
        BudgetResponseDto budgetResponseDto = new BudgetResponseDto(1L, 10000, 1000, 9000, Arrays.asList(expenseDto));
        given(expenseService.readExpenses(any(),any())).willReturn(budgetResponseDto);
        restDocsMockMvc.perform(get("/travelgroup/1/expenses").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("expense_get_expenses",preprocessRequest(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor()
                ),preprocessResponse(RestDocUtil.MockMvcConfig.prettyPrintPreProcessor())));

    }

    @Test
    void 지출_추가() throws Exception{
        Group group = createGroup();
        Budget budget = createBudget(group);
        Expense expense = createExpense();

        given(groupService.findById(any())).willReturn(group);
        given(budgetService.findByGroup(any())).willReturn(budget);
        given(expenseService.addExpense(any(),any(),any(),any())).willReturn(expense);

        ExpenseDto expenseDto = new ExpenseDto(expense);
        String json = objectMapper.writeValueAsString(expenseDto);

        restDocsMockMvc.perform(post("/travelgroup/1/expenses").session(session)
                .content(json)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("expense-update-expense"));
    }

    @Test
    void 지출_전체_삭제() throws Exception{
        Group group = createGroup();
        given(groupService.findById(any())).willReturn(group);
        doNothing().when(expenseService).deleteExpense(any(),any());
        restDocsMockMvc.perform(delete("/travelgroup/1/expenses").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("expense-delete-all"));
    }

    @Test
    void 지출_단건_수정() throws Exception{
        Expense expense = createExpense();
        given(expenseService.findById(any())).willReturn(expense);
        doNothing().when(expenseService).updateExpense(any(),any());

        ExpenseDto expenseDto = new ExpenseDto(expense);
        String json = objectMapper.writeValueAsString(expenseDto);
        restDocsMockMvc.perform(put("/travelgroup/1/expenses/1").session(session)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("expense-update-expense"));
    }

    @Test
    void 지출_단건_삭제() throws Exception{
        Expense expense = createExpense();
        given(expenseService.findById(any())).willReturn(expense);
        doNothing().when(expenseService).deleteExpense(any(),any());

        restDocsMockMvc.perform(delete("/travelgroup/1/expenses/1").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("expense-delete-expense"));
    }

    private Expense createExpense() {
        return new Expense(Expense.Category.TRANSPORTATION,"detail",1000L);
    }

    private Budget createBudget(Group group) {
        return new Budget(group, 10000L);
    }

    private Member createMember(){
        return new Member(NAME, PASSWORD,IMAGE,EMAIL);
    }

    private Group createGroup() {
        return Group.builder().groupName("groupName").groupsIntroduction("This is Group").build();
    }
}
