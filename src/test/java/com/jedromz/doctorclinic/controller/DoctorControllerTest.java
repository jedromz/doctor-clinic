package com.jedromz.doctorclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jedromz.doctorclinic.DoctorClinicApplication;
import com.jedromz.doctorclinic.model.Doctor;
import com.jedromz.doctorclinic.model.DoctorSpecialization;
import com.jedromz.doctorclinic.model.auth.AppUser;
import com.jedromz.doctorclinic.model.command.CreateDoctorCommand;
import com.jedromz.doctorclinic.repository.DoctorRepository;
import com.jedromz.doctorclinic.service.auth.AppUserServiceImpl;
import com.jedromz.doctorclinic.service.impl.DoctorServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DoctorClinicApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@RequiredArgsConstructor
class DoctorControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DoctorServiceImpl doctorService;

    @Test
    @SneakyThrows
    void shouldGetSingleDoctor() {
        //given
        Doctor newDoctor = Doctor.builder()
                .firstname("TEST_FIRSTNAME")
                .lastname("TEST_LASTNAME")
                .specialization(DoctorSpecialization.CARDIOLOGIST)
                .rate(new BigDecimal("350.99"))
                .nip("97922491833")
                .build();
        Doctor savedDoctor = doctorService.save(newDoctor);
        long doctorId = newDoctor.getId();

        //when
        MvcResult mvcResult = postman.perform(get("/doctors/{id}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value(newDoctor.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(newDoctor.getLastname()))
                .andExpect(jsonPath("$.rate").value(newDoctor.getRate()))
                .andExpect(jsonPath("$.nip").value(newDoctor.getNip()))
                .andExpect(jsonPath("$.specialization").value(newDoctor.getSpecialization().name()))
                .andReturn();
        //then
        Doctor doctor = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Doctor.class);
        assertEquals(savedDoctor.getFirstname(), doctor.getFirstname());
        assertEquals(savedDoctor.getLastname(), doctor.getLastname());
        assertEquals(savedDoctor.getSpecialization(), doctor.getSpecialization());
        assertEquals(savedDoctor.getRate(), doctor.getRate());
        assertEquals(savedDoctor.getNip(), doctor.getNip());
    }

    @Test
    void shouldNotGetDoctorWithBadId() throws Exception {
        long badId = 1L;
        postman.perform(get("/doctors/{id}", badId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse().getErrorMessage();
    }

    @Test
    void shouldAddDoctor() throws Exception {
        //given
        CreateDoctorCommand command = CreateDoctorCommand.builder()
                .firstname("TEST_FIRSTNAME")
                .lastname("TEST_LASTNAME")
                .specialization(String.valueOf(DoctorSpecialization.CARDIOLOGIST))
                .rate(new BigDecimal("350.99"))
                .nip("97922491833")
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        String response = postman.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int savedId = JsonPath.read(response, "id");
        //then
        postman.perform(get("/doctors/" + savedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value(command.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(command.getLastname()))
                .andExpect(jsonPath("$.rate").value(command.getRate()))
                .andExpect(jsonPath("$.nip").value(command.getNip()))
                .andExpect(jsonPath("$.specialization").value(command.getSpecialization()))
                .andReturn();
    }

    @Test
    void shouldNotAddDoctor() throws Exception {
        //given
        CreateDoctorCommand command = CreateDoctorCommand.builder().build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        postman.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
