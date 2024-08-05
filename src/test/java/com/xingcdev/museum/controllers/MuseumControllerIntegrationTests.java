package com.xingcdev.museum.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MuseumControllerIntegrationTests {

    private final MockMvc mockMvc;

    @Autowired
    public MuseumControllerIntegrationTests(MockMvc mockMvc
    ) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void getMuseumsShouldReturnMuseums() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/museums")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée Chintreuil"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée Picasso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name").value("musée du Louvre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].name").value("musée du bois"));

    }

    @Test
    public void getMuseumsShouldReturnSortingMuseums() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/museums?sort=name:desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée du bois"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name").value("musée Picasso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].name").value("musée Chintreuil"));
    }

    @Test
    public void getMuseumsShouldReturnSearchingMuseums() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/museums?q=du&sort=name:desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée du bois"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"));
    }

    @Test
    public void getMuseumsShouldReturnMuseumInParis() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/museums?city=paris")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée Picasso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"));

    }

    @Test
    public void getMuseumShouldReturnMuseumWhenExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/museums/dc739b92-f0a0-47bf-a49e-0f6296dc726a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value("dc739b92-f0a0-47bf-a49e-0f6296dc726a")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value("musée du Louvre")
                );
    }

    @Test
    public void getMuseumShouldReturn404WhenNoExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/museums/???")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );
    }

}
