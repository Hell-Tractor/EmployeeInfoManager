package com.employeeinfomanager;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.aop.UserToken;
import com.employeeinfomanager.common.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static String rootToken;

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
    }

    @Test
    public void createUserTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER)
                    .header("authorization", rootToken)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(4))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void loginTest0() throws Exception {
        String requestJson = "{ username: \"test0\", password: \"123456\" }";
        String response = this.mockMvc.perform(MockMvcRequestBuilders.post(LOGIN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readValue(response, Map.class).get("data").toString();
        JwtHelper jwtHelper = new JwtHelper();
        UserToken decryptedToken = jwtHelper.verifyTokenAndGetClaims(token);
        Assertions.assertEquals("test0", decryptedToken.getUsername());
        Assertions.assertEquals(1, decryptedToken.getUserId());
        Assertions.assertEquals(AuditLevel.ADMIN, decryptedToken.getUserLevel());
        Assertions.assertEquals(0, decryptedToken.getDepartId());
    }
}
