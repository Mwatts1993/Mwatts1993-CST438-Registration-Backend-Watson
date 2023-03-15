package com.cst438;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {StudentController.class})
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
    static final String URL = "http://localhost:8080";
    public static final int TEST_STUDENT_ID = 1;
    public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
    public static final String TEST_STUDENT_NAME = "test";
    public static final String TEST_STUDENT_STATUS = null;
    public static final int TEST_STUDENT_STATUS_CODE = 0;

    @MockBean
    StudentRepository studentRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void studentNoExistAddStudent() throws Exception {
        MockHttpServletResponse response;

        Student student = new Student();
        student.setEmail(TEST_STUDENT_EMAIL);
        student.setStudent_id(TEST_STUDENT_ID);
        student.setName(TEST_STUDENT_NAME);
        student.setStatus(TEST_STUDENT_STATUS);
        student.setStatusCode(TEST_STUDENT_STATUS_CODE);

        StudentDTO studentDTO = new StudentDTO("notarealemail@gmail.com", "notarealname", TEST_STUDENT_STATUS);

        given(studentRepository.findByEmail(studentDTO.studentEmail)).willReturn(null);

        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/student")
                                .content(asJsonString(studentDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        int status = response.getStatus();
        assertEquals(200, status);

        given(studentRepository.findByEmail(studentDTO.studentEmail)).willReturn(new Student());

        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/student")
                                .content(asJsonString(studentDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
    }

    @Test
    public void studentExistsAddStudent() throws Exception {
        MockHttpServletResponse response;

        Student student = new Student();
        student.setEmail(TEST_STUDENT_EMAIL);
        student.setStudent_id(TEST_STUDENT_ID);
        student.setName(TEST_STUDENT_NAME);
        student.setStatus(TEST_STUDENT_STATUS);
        student.setStatusCode(TEST_STUDENT_STATUS_CODE);

        StudentDTO studentDTO = new StudentDTO("notarealemail@gmail.com", "notarealname", TEST_STUDENT_STATUS);

        given(studentRepository.findByEmail(studentDTO.studentEmail)).willReturn(student);

        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/student")
                                .content(asJsonString(studentDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        int status = response.getStatus();
        assertEquals(400, status);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    public void updateStudent() throws Exception {
        MockHttpServletResponse response;

        StudentDTO studentDTO = new StudentDTO(TEST_STUDENT_EMAIL,TEST_STUDENT_NAME,"HOLD");

        Student student = new Student();
        student.setEmail(TEST_STUDENT_EMAIL);
        student.setStudent_id(TEST_STUDENT_ID);
        student.setName(TEST_STUDENT_NAME);
        student.setStatus(TEST_STUDENT_STATUS);
        student.setStatusCode(TEST_STUDENT_STATUS_CODE);

        System.out.println(student.getEmail());
        System.out.println(studentDTO.studentEmail);

        given(studentRepository.findByStudentId(TEST_STUDENT_ID)).willReturn(student);
        given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);

        response = mvc.perform(
                        MockMvcRequestBuilders
                                .put("/student/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(studentDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        int status = response.getStatus();

        assertEquals(200,status);
        assertEquals(student.getStatus(),"HOLD");

        System.out.println(student.getStatus());

        studentDTO.studentStatus = null;

        response = mvc.perform(
                        MockMvcRequestBuilders
                                .put("/student/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(studentDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200,status);
        assertNull(student.getStatus());
    }

    private static String asJsonString(final Object obj) {
        try {
            System.out.println(obj.toString());
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    private static <T> T fromJsonString(String str, Class<T> valueType) {
//        try {
//            return new ObjectMapper().readValue(str, valueType);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
