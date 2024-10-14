package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.DepartDao;
import com.employeeinfomanager.dao.UserDao;
import com.employeeinfomanager.dao.bo.Depart;
import com.employeeinfomanager.dao.bo.User;
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
public class DepartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartDao departDao;
    @MockBean
    private UserDao userDao;

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
        Mockito.when(userDao.retrieveByDepartId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DEPART)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));
    }

    @Test
    public void deleteDepartTest1() throws Exception {
        Mockito.when(userDao.retrieveByDepartId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DEPART)
                        .header("authorization", rootToken)
                        .queryParam("id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.RESOURCE_NOT_EXIST.getCode()));
    }

    @Test
    public void deleteDepartTest2() throws Exception {
        User user = new User("test", "test", AuditLevel.ADMIN, 1L);
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userDao.retrieveByDepartId(Mockito.anyLong())).thenReturn(users);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DEPART)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.DEPART_STILL_IN_USE.getCode()));
    }

    @Test
    public void updateDepartTest0() throws Exception {
        String requestJson = "{ \"id\": 1, \"name\": \"测试公司\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_DEPART)
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
