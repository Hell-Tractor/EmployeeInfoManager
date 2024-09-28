package com.employeeinfomanager;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.EmploymentDao;
import com.employeeinfomanager.dao.StaffDao;
import com.employeeinfomanager.dao.bo.Staff;
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
public class StaffControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmploymentDao employmentDao;
    @Autowired
    private StaffDao staffDao;
    private static String rootToken;

    /*---------------------API LIST---------------------*/
    private static final String CREATE_STAFF = "/staff";
    private static final String DELETE_STAFF = "/staff";
    private static final String UPDATE_STAFF = "/staff/update";
    private static final String GET_STAFF = "/staff";
    /*--------------------------------------------------*/

    @BeforeAll
    public static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        rootToken = jwtHelper.createToken(0L, "root", AuditLevel.ROOT, -1L, 3600);
    }

    @Test
    public void createStaffTest0() throws Exception {
        String requestJson = "{ \"name\": \"测试\", \"image\": \"test_image\", \"bornYear\": 1980, \"personId\": 142700198001010000, \"experience\": \"\", \"physicalCondition\": \"\", \"appendix\": \"\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_STAFF)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.CREATED.getCode()));
    }

    @Test
    public void createStaffTest1() throws Exception {
        String requestJson = "{ \"name\": \"测试 \", \"image\": \"test_image\", \"bornYear\": 1980, \"personId\": 142700198001010000, \"experience\": \"\", \"physicalCondition\": \"\", \"appendix\": \"\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_STAFF)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.FIELD_INVALID.getCode()));
    }

    @Test
    public void createStaffTest2() throws Exception {
        String requestJson = "{ \"name\": \"测试\", \"image\": \"test_image\", \"bornYear\": 1980, \"personId\": 142700198501010000, \"experience\": \"\", \"physicalCondition\": \"\", \"appendix\": \"\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CREATE_STAFF)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.STAFF_EXIST.getCode()));
    }

    @Test
    public void deleteRiskTagTest0() throws Exception {
        Mockito.when(employmentDao.retrieveEmploymentIdsByStaffId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STAFF)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));
    }

    @Test
    public void deleteRiskTagTest1() throws Exception {
        Mockito.when(employmentDao.retrieveEmploymentIdsByStaffId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STAFF)
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
        Mockito.when(employmentDao.retrieveEmploymentIdsByStaffId(Mockito.anyLong())).thenReturn(ids);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STAFF)
                        .header("authorization", rootToken)
                        .queryParam("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.STAFF_STILL_IN_USE.getCode()));
    }

    @Test
    public void updateStaffTest0() throws Exception {
        String requestJson = "{ \"id\": 1, \"name\": \"测试\", \"image\": \"test_image\", \"bornYear\": 1980, \"personId\": 142700198001010000, \"experience\": \"\", \"physicalCondition\": \"\", \"appendix\": \"\" }";
        this.mockMvc.perform(MockMvcRequestBuilders.get(UPDATE_STAFF)
                        .header("authorization", rootToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()));

        Staff staff = this.staffDao.findById(1L);
        Assertions.assertEquals("测试", staff.getName());
    }

    @Test
    public void getStaffTest0() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_STAFF)
                        .header("authorization", rootToken)
                        .queryParam("personId", "142700198501010000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.OK.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("张三"));
    }

    @Test
    public void getStaffTest1() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_STAFF)
                        .header("authorization", rootToken)
                        .queryParam("personId", "142700198501010001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errNo").value(ReturnNo.STAFF_NOT_EXIST.getCode()));
    }
}
