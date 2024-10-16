package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.EmploymentDao;
import com.employeeinfomanager.dao.RiskTagDao;
import com.employeeinfomanager.dao.bo.RiskTag;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RiskTagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RiskTagDao riskTagDao;
    @MockBean
    private EmploymentDao employmentDao;

    private static String rootToken;

    /*---------------------API LIST---------------------*/
    private static final String CREATE_RISK_TAG = "/risk_tag";
    private static final String DELETE_RISK_TAG = "/risk_tag";
    private static final String UPDATE_RISK_TAG = "/risk_tag/update";
    private static final String RETRIEVE_RISK_TAG = "/risk_tag";
    /*--------------------------------------------------*/

    @BeforeAll
    public static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        rootToken = jwtHelper.createToken(0L, "root", AuditLevel.ROOT, -1L, 3600);
    }

    @Test
    public void createRiskTagTest0() throws Exception {
        String requestJson = "{ \"name\": \"溶解\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_RISK_TAG)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.CREATED.getCode()));
    }

    @Test
    public void createRiskTagTest1() throws Exception {
        String requestJson = "{ \"name\": \"溶解解解解解解解解\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_RISK_TAG)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.FIELD_INVALID.getCode()));
    }

    @Test
    public void createRiskTagTest2() throws Exception {
        String requestJson = "{ \"name\": \"高处\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_RISK_TAG)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RISK_TAG_EXIST.getCode()));
    }

    @Test
    public void deleteRiskTagTest0() throws Exception {
        Mockito.when(employmentDao.retrieveDistinctEmploymentIdsByRiskTagId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RISK_TAG)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));
    }

    @Test
    public void deleteRiskTagTest1() throws Exception {
        Mockito.when(employmentDao.retrieveDistinctEmploymentIdsByRiskTagId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RISK_TAG)
                        .header("authorization", rootToken)
                        .queryParam("id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void deleteRiskTagTest2() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        Mockito.when(employmentDao.retrieveDistinctEmploymentIdsByRiskTagId(Mockito.anyLong())).thenReturn(ids);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RISK_TAG)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RISK_TAG_STILL_IN_USE.getCode()));
    }

    @Test
    public void updateRiskTagTest0() throws Exception {
        String requestJson = "{\"id\": 1, \"name\": \"测试\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_RISK_TAG)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));

        RiskTag tag = this.riskTagDao.findById(1L);
        Assertions.assertEquals("测试", tag.getName());
    }

    @Test
    public void retrieveRiskTagsTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RISK_TAG)
                        .header("authorization", rootToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()").value(6));
    }
}
