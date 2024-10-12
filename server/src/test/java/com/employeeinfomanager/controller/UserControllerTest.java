package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.aop.UserToken;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.Utils;
import com.employeeinfomanager.dao.DepartDao;
import com.employeeinfomanager.dao.bo.Depart;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartDao departDao;

    private static String rootToken;
    private static String adminToken;

    /*---------------------API LIST---------------------*/
    private static final String CREATE_USER = "/user/create";
    private static final String LOGIN = "/user/login";
    private static final String RETRIEVE_USERS = "/user";
    private static final String DELETE_USER_FORCE = "/user/delete/force";
    private static final String DELETE_SELF = "/user/delete";
    private static final String UPDATE_PASSWORD = "/user/update";
    /*--------------------------------------------------*/

    @BeforeAll
    public static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        rootToken = jwtHelper.createToken(0L, "root", AuditLevel.ROOT, -1L, 3600);
        adminToken = jwtHelper.createToken(1L, "test0", AuditLevel.ADMIN, -1L, 3600);
    }

    @Test
    public void createUserTest0() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String requestJson = "{ \"username\": \"__test\", \"password\": \"123456\", \"departId\": 0 }";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER)
                    .header("authorization", rootToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.CREATED.getCode()));
    }

    @Test
    public void createUserTest1() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String requestJson = "{ \"username\": \"__test\", \"password\": \"123456\", \"departId\": 0 }";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.AUTH_LOGIN_REQUIRED.getCode()));
    }

    @Test
    public void createUserTest2() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String requestJson = "{ \"username\": \"__test\", \"password\": \"123456\", \"departId\": 0 }";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.AUTH_NO_RIGHT.getCode()));
    }

    @Test
    public void createUserTest3() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String requestJson = "{ \"username\": \"te\", \"password\": \"123456\", \"departId\": 0 }";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER)
                        .header("authorization", rootToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.FIELD_INVALID.getCode()));
    }

    @Test
    public void createUserTest4() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String requestJson = "{ \"username\": \"__test\", \"password\": \"测试测试测试\", \"departId\": 0 }";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER)
                        .header("authorization", rootToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.FIELD_INVALID.getCode()));
    }

    public String loginAndGetToken(String username, String password) throws Exception {
        String salt = this.mockMvc.perform(MockMvcRequestBuilders.get(LOGIN)
                        .queryParam("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        salt = Utils.getJsonField(salt, "data", String.class);

        password = Utils.getMD5((password + salt).getBytes());
        String requestJson = String.format("{ \"username\": \"%s\", \"password\": \"%s\", \"salt\": \"%s\" }", username, password, salt);
        String response = this.mockMvc.perform(MockMvcRequestBuilders.post(LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return Utils.getJsonField(response, "data.token", String.class);
    }

    @Test
    public void loginTest0() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String token = this.loginAndGetToken("test0", "123456");
        JwtHelper jwtHelper = new JwtHelper();
        UserToken decryptedToken = jwtHelper.verifyTokenAndGetClaims(token);
        Assertions.assertEquals("test0", decryptedToken.getUsername());
        Assertions.assertEquals(AuditLevel.ADMIN, decryptedToken.getUserLevel());
        Assertions.assertEquals(1, decryptedToken.getDepartId());
    }

    @Test
    public void retrieveUserTest0() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_USERS)
                    .header("authorization", rootToken)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(4));
    }

    @Test
    public void retrieveUserTest1() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_USERS)
                        .header("authorization", rootToken)
                        .queryParam("pageSize", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(1));
    }

    @Test
    public void retrieveUserTest2() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_USERS)
                        .header("authorization", rootToken)
                        .queryParam("page", "2")
                        .queryParam("pageSize", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(1));
    }

    @Test
    public void deleteUserForceTest0() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_FORCE)
                        .header("authorization", rootToken)
                        .queryParam("userId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));
    }

    @Test
    public void deleteUserForceTest1() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_FORCE)
                        .header("authorization", rootToken)
                        .queryParam("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.AUTH_NO_RIGHT.getCode()));
    }

    @Test
    public void deleteUserForceTest2() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_FORCE)
                        .header("authorization", adminToken)
                        .queryParam("userId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.AUTH_NO_RIGHT.getCode()));
    }

    @Test
    public void deleteSelfTest0() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String token = this.loginAndGetToken("test0", "123456");
        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_SELF)
                        .header("authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteSelfTest1() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String token = this.loginAndGetToken("root", "123456");
        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_SELF)
                        .header("authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.AUTH_NO_RIGHT.getCode()));
    }

    @Test
    public void updatePasswordTest0() throws Exception {
        Mockito.when(departDao.findById(Mockito.anyLong())).thenReturn(new Depart(-1L, "测试部门"));

        String token = this.loginAndGetToken("root", "123456");

        String updatePasswordJson = "{ \"oldPassword\": \"123456\", \"newPassword\": \"123456abc\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_PASSWORD)
                        .header("authorization", token)
                        .content(updatePasswordJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        this.loginAndGetToken("root", "123456abc");
    }
}
