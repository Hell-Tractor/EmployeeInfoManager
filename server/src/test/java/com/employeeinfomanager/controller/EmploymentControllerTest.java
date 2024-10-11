package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.EmploymentDao;
import com.employeeinfomanager.dao.bo.Employment;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmploymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmploymentDao employmentDao;
    private static String rootToken;
    private static String admin1Token;
    private static String admin2Token;

    /*---------------------API LIST---------------------*/
    private static final String CREATE_EMPLOYMENT = "/employment";
    private static final String DELETE_EMPLOYMENT = "/employment";
    private static final String GET_EMPLOYMENT = "/employment";
    private static final String UPDATE_EMPLOYMENT = "/employment/update";
    private static final String RETRIEVE_DEPART_EMPLOYMENTS = "/employment/depart/{departId}/all";
    private static final String ADD_RISK_TAG = "/employment/{employmentId}/risk_tag";
    private static final String REMOVE_RISK_TAG = "/employment/{employmentId}/risk_tag";
    /*--------------------------------------------------*/

    @BeforeAll
    public static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        rootToken = jwtHelper.createToken(1L, "root", AuditLevel.ROOT, -1L, 3600);
        admin1Token = jwtHelper.createToken(3L, "test1", AuditLevel.ADMIN, 1L, 3600);
        admin2Token = jwtHelper.createToken(4L, "test2", AuditLevel.ADMIN, 2L, 3600);
    }

    @Test
    public void createEmploymentTest0() throws Exception {
        String requestJson = "{\"staffId\": 1, \"departId\": 1, \"project\": \"test project\", \"validSince\": \"2023-01-01\", \"validUntil\": \"2024-01-01\", \"workPermit\": \"\", \"riskTagIds\": [ 1, 3 ], \"violation\": \"test violation\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_EMPLOYMENT)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.CREATED.getCode()));
    }

    @Test
    public void createEmploymentTest1() throws Exception {
        String requestJson = "{\"staffId\": 1, \"departId\": 1, \"project\": \"test project\", \"validSince\": \"2023-01-01\", \"validUntil\": \"2024-01-01\", \"workPermit\": \"\", \"riskTagIds\": [ 1, 3 ], \"violation\": \"test violation\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_EMPLOYMENT)
                        .header("authorization", admin1Token)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.CREATED.getCode()));
    }

    @Test
    public void createEmploymentTest2() throws Exception {
        String requestJson = "{\"staffId\": 1, \"departId\": 1, \"project\": \"test project\", \"validSince\": \"2023-01-01\", \"validUntil\": \"2024-01-01\", \"workPermit\": \"\", \"riskTagIds\": [ 1, 3 ], \"violation\": \"test violation\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_EMPLOYMENT)
                        .header("authorization", admin2Token)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.AUTH_NO_RIGHT.getCode()));
    }

    @Test
    public void createEmploymentTest3() throws Exception {
        String requestJson = "{\"staffId\": 100, \"departId\": 1, \"project\": \"test project\", \"validSince\": \"2023-01-01\", \"validUntil\": \"2024-01-01\", \"workPermit\": \"\", \"riskTagIds\": [ 1, 3 ], \"violation\": \"test violation\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_EMPLOYMENT)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void createEmploymentTest4() throws Exception {
        String requestJson = "{\"staffId\": 1, \"departId\": 100, \"project\": \"test project\", \"validSince\": \"2023-01-01\", \"validUntil\": \"2024-01-01\", \"workPermit\": \"\", \"riskTagIds\": [ 1, 3 ], \"violation\": \"test violation\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_EMPLOYMENT)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void deleteEmploymentTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_EMPLOYMENT)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));
    }

    @Test
    public void deleteEmploymentTest1() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_EMPLOYMENT)
                        .header("authorization", rootToken)
                        .queryParam("id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void getEmploymentByIdTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_EMPLOYMENT)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.staff.name").value("张三"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.depart.name").value("公司0"));
    }

    @Test
    public void getEmploymentByIdTest1() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_EMPLOYMENT)
                        .queryParam("id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void updateEmploymentTest0() throws Exception {
        String requestJson = "{ \"id\": 1, \"staffId\": 1, \"departId\": 1, \"project\": \"modified project\", \"validSince\": \"2023-01-01\", \"validUntil\": \"2024-01-01\", \"workPermit\": \"\", \"violation\": \"test violation\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.get(UPDATE_EMPLOYMENT)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));

        Employment employment = this.employmentDao.findById(1L);
        Assertions.assertEquals("modified project", employment.getProject());
    }

    @Test
    public void retrieveEmploymentsByDepartIdTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DEPART_EMPLOYMENTS, 1L)
                        .header("authorization", rootToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()").value(4));
    }

    @Test
    public void addRiskTagTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADD_RISK_TAG, 1L)
                        .header("authorization", rootToken)
                        .queryParam("riskTagId", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.CREATED.getCode()));
    }

    @Test
    public void addRiskTagTest1() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADD_RISK_TAG, 100L)
                        .header("authorization", rootToken)
                        .queryParam("riskTagId", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void addRiskTagTest2() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADD_RISK_TAG, 1L)
                        .header("authorization", rootToken)
                        .queryParam("riskTagId", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void addRiskTagTest3() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADD_RISK_TAG, 1L)
                        .header("authorization", rootToken)
                        .queryParam("riskTagId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RISK_TAG_ALREADY_IN_EMPLOYMENT.getCode()));
    }

    @Test
    public void removeRiskTagTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(REMOVE_RISK_TAG, 1L)
                        .header("authorization", rootToken)
                        .queryParam("riskTagId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));
    }

    @Test
    public void removeRiskTagTest1() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(REMOVE_RISK_TAG, 100L)
                        .header("authorization", rootToken)
                        .queryParam("riskTagId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void removeRiskTagTest2() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(REMOVE_RISK_TAG, 1L)
                        .header("authorization", rootToken)
                        .queryParam("riskTagId", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RISK_TAG_NOT_FOUND_IN_EMPLOYMENT.getCode()));
    }
}
