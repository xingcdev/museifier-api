package com.xingcdev.museum.controllers;

import com.xingcdev.museum.services.MuseumService;
import com.xingcdev.museum.utils.AuthUtils;
import com.xingcdev.museum.utils.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MuseumControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final static Logger logger = LoggerFactory.getLogger(VisitControllerIntegrationTest.class);

    private final MuseumService museumService;

    private static String accessToken;

    @Autowired
    public MuseumControllerIntegrationTests(MockMvc mockMvc,
                                            MuseumService museumService) {
        this.mockMvc = mockMvc;
        this.museumService = museumService;
    }

    @BeforeAll
    static void setup() {
        logger.info("Authenticating...");

        var authUtils = new AuthUtils();
        var result = authUtils.authenticate();
        accessToken = result.getAccessToken();
    }

    @Test
    public void getMuseumsShouldReturnMuseums() throws Exception {
        museumService.save(TestDataUtil.createMuseumLeLouvre());
        museumService.save(TestDataUtil.createMuseumChintreuil());
        museumService.save(TestDataUtil.createMuseumMuseeDuBois());

        mockMvc.perform(MockMvcRequestBuilders.get("/museums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée Chintreuil"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name").value("musée du bois"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageInfo.totalResults").value(3));
    }

    @Test
    public void getMuseumsShouldReturnSortingMuseums() throws Exception {
        museumService.save(TestDataUtil.createMuseumLeLouvre());
        museumService.save(TestDataUtil.createMuseumChintreuil());
        museumService.save(TestDataUtil.createMuseumMuseeDuBois());

        mockMvc.perform(MockMvcRequestBuilders.get("/museums?sort=name:desc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée du bois"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name").value("musée Chintreuil"));
    }

    @Test
    public void getMuseumsShouldReturnSearchingMuseums() throws Exception {
        museumService.save(TestDataUtil.createMuseumLeLouvre());
        museumService.save(TestDataUtil.createMuseumChintreuil());
        museumService.save(TestDataUtil.createMuseumMuseeDuBois());

        mockMvc.perform(MockMvcRequestBuilders.get("/museums?q=du&sort=name:desc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée du bois"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"));
    }

    @Test
    public void getMuseumsShouldReturnMuseumInParis() throws Exception {
        museumService.save(TestDataUtil.createMuseumLeLouvre());
        museumService.save(TestDataUtil.createMuseumPicasso());
        museumService.save(TestDataUtil.createMuseumMuseeDuBois());

        mockMvc.perform(MockMvcRequestBuilders.get("/museums?city=paris")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée Picasso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"));

    }

    @Test
    public void getMuseumShouldReturnMuseumWhenExist() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());

        mockMvc.perform(MockMvcRequestBuilders.get("/museums/" + museumInDb.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(museumInDb.getId().toString())
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value("musée du Louvre")
                );
    }

    @Test
    public void getMuseumShouldReturn404WhenNoExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/museums/???")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );
    }

}
