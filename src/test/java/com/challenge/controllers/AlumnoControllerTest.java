package com.challenge.controllers;

import com.challenge.dtos.AlumnoDto;
import com.challenge.services.AlumnoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlumnoController.class)
@DisplayName("Pruebas Unitarias para AlumnoController")
class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoService alumnoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debería guardar un alumno y devolver estado 201 Created")
    void shouldGuardarAlumnoAndReturn201Created() throws Exception {
        AlumnoDto inputDto = new AlumnoDto(null, "Roberto", "Carlos", 12345678, 100, "Calle A 123", 25);
        AlumnoDto outputDto = new AlumnoDto(1L, "Roberto", "Carlos", 12345678, 100, "Calle A 123", 25);

        when(alumnoService.guardarAlumno(any(AlumnoDto.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Roberto")))
                .andExpect(jsonPath("$.dni", is(12345678)));
    }

    @Test
    @DisplayName("Debería obtener todos los alumnos y devolver estado 200 OK")
    void shouldGetAllAlumnosAndReturn200Ok() throws Exception {

        AlumnoDto alumno1 = new AlumnoDto(1L, "Laura", "Vega", 11111111, 200, "Av. Siempre Viva 10", 22);
        AlumnoDto alumno2 = new AlumnoDto(2L, "Diego", "Silva", 22222222, 201, "Calle B 456", 24);
        List<AlumnoDto> alumnosList = Arrays.asList(alumno1, alumno2);

        when(alumnoService.findAllAlumnos()).thenReturn(alumnosList);

        mockMvc.perform(get("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Laura")))
                .andExpect(jsonPath("$[1].dni", is(22222222)));
    }
}
