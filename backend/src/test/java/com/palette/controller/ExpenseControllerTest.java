package com.palette.controller;

import com.palette.controller.util.RestDocUtil;
import com.palette.domain.group.Budget;
import com.palette.domain.group.Expense;
import com.palette.domain.group.Group;
import com.palette.dto.request.ExpenseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
public class ExpenseControllerTest extends RestDocControllerTest{

    @Autowired ExpenseController expenseController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider){
        this.restDocsMockMvc = RestDocUtil.successRestDocsMockMvc(provider, expenseController);
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
        //String json2 = "{\"category\":\"교통\",\"detail\":\"교통\",\"price\":\"1000\"}";

        restDocsMockMvc.perform(post("/api/travelgroup/1/expenses").header(AUTH, TOKEN)
                .content(json)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("expense-create-expense"));
    }

    @Test
    void 지출_전체_삭제() throws Exception{
        Group group = createGroup();
        given(groupService.findById(any())).willReturn(group);
        doNothing().when(expenseService).deleteExpense(any(),any());
        restDocsMockMvc.perform(delete("/api/travelgroup/1/expenses").header(AUTH, TOKEN))
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
        restDocsMockMvc.perform(put("/api/travelgroup/1/expenses/1").header(AUTH, TOKEN)
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

        restDocsMockMvc.perform(delete("/api/travelgroup/1/expenses/1").header(AUTH, TOKEN))
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
}
