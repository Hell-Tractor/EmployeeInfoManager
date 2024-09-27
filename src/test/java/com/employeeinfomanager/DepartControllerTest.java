package com.employeeinfomanager;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.DepartDao;
import com.employeeinfomanager.dao.bo.Depart;
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
public class DepartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartDao departDao;

    private static String rootToken;

    /*---------------------API LIST---------------------*/
    private static final String CREATE_DEPART = "/depart";
    private static final String DELETE_DEPART = "/depart";
    private static final String UPDATE_DEPART = "/depart/update";
    private static final String RETRIEVE_DEPARTS = "/depart";
    /*--------------------------------------------------*/

    @BeforeAll
    public static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        rootToken = jwtHelper.createToken(0L, "root", AuditLevel.ROOT, -1L, 3600);
    }

    @Test
    public void createDepartTest0() throws Exception {
        String requestJson = "{ \"name\": \"某某公司\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_DEPART)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.CREATED.getCode()));
    }

    @Test
    public void createDepartTest1() throws Exception {
        String requestJson = "{ \"name\": \"1\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_DEPART)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.FIELD_INVALID.getCode()));
    }

    @Test
    public void createDepartTest2() throws Exception {
        String requestJson = "{ \"name\": \"公司0\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_DEPART)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.DEPART_EXIST.getCode()));
    }

    @Test
    public void deleteDepartTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DEPART)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));
    }

    @Test
    public void deleteDepartTest1() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DEPART)
                        .header("authorization", rootToken)
                        .queryParam("id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void updateDepartTest0() throws Exception {
        String requestJson = "{ \"id\": 1, \"name\": \"测试公司\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.get(UPDATE_DEPART)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));

        Depart depart = this.departDao.findById(1L);
        Assertions.assertEquals("测试公司", depart.getName());
    }

    @Test
    public void retrieveDepartsTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DEPARTS)
                        .header("authorization", rootToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()").value(2));
    }
}
