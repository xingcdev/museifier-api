package com.xingcdev.museum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xingcdev.museum.domain.dto.VisitRequestBody;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles({"development", "test"})
public class MuseumControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final static Logger logger = LoggerFactory.getLogger(VisitControllerIntegrationTest.class);

    private final MuseumService museumService;

    private final ObjectMapper objectMapper;

    private final TestDataUtil testDataUtil;

    private static String accessToken;

    @Autowired
    public MuseumControllerIntegrationTests(MockMvc mockMvc,
                                            MuseumService museumService, ObjectMapper objectMapper, TestDataUtil testDataUtil) {
        this.mockMvc = mockMvc;
        this.museumService = museumService;
        this.objectMapper = objectMapper;
        this.testDataUtil = testDataUtil;
    }

    @BeforeAll
    public static void setup(@Autowired AuthUtils authUtils) {
        logger.info("Authenticating...");
        var result = authUtils.authenticate();
        accessToken = result.getAccessToken();
    }

    @Test
    public void getMuseumsShouldReturnMuseums() throws Exception {
        museumService.save(testDataUtil.createMuseumLeLouvre());
        museumService.save(testDataUtil.createMuseumChintreuil());
        museumService.save(testDataUtil.createMuseumMuseeDuBois());

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
        museumService.save(testDataUtil.createMuseumLeLouvre());
        museumService.save(testDataUtil.createMuseumChintreuil());
        museumService.save(testDataUtil.createMuseumMuseeDuBois());

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
        museumService.save(testDataUtil.createMuseumLeLouvre());
        museumService.save(testDataUtil.createMuseumChintreuil());
        museumService.save(testDataUtil.createMuseumMuseeDuBois());

        mockMvc.perform(MockMvcRequestBuilders.get("/museums?q=du&sort=name:desc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée du bois"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"));
    }

    @Test
    public void getMuseumsShouldReturnMuseumInParis() throws Exception {
        museumService.save(testDataUtil.createMuseumLeLouvre());
        museumService.save(testDataUtil.createMuseumPicasso());
        museumService.save(testDataUtil.createMuseumMuseeDuBois());

        mockMvc.perform(MockMvcRequestBuilders.get("/museums?city=paris")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("musée Picasso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("musée du Louvre"));

    }

    @Test
    public void getMuseumShouldReturnMuseumWhenExist() throws Exception {
        var museumInDb = museumService.save(testDataUtil.createMuseumLeLouvre());

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

    @Test
    public void getMuseumsShouldReturnVisitedMuseums() throws Exception {
        var museumLeLouvreInDb = museumService.save(testDataUtil.createMuseumLeLouvre());
        var museumPicassoInDb = museumService.save(testDataUtil.createMuseumPicasso());

        var visitDto1 = VisitRequestBody
                .builder()
                .title("First visit at Le Louvre")
                .rating(3)
                .visitDate(LocalDate.of(2024, 1, 1))
                .comment("Comment")
                .museumId(museumLeLouvreInDb.getId())
                .build();
        String visitDtoJson1 = objectMapper.writeValueAsString(visitDto1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson1)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        var visitDto2 = VisitRequestBody
                .builder()
                .title("Second visit at Le Louvre")
                .rating(3)
                .visitDate(LocalDate.of(2024, 1, 2))
                .comment("Comment")
                .museumId(museumLeLouvreInDb.getId())
                .build();
        String visitDtoJson2 = objectMapper.writeValueAsString(visitDto2);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson2)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        var visitDto3 = VisitRequestBody
                .builder()
                .title("First visit at Picasso")
                .rating(3)
                .visitDate(LocalDate.of(2024, 1, 3))
                .comment("Comment")
                .museumId(museumPicassoInDb.getId())
                .build();
        String visitDtoJson3 = objectMapper.writeValueAsString(visitDto3);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson3)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/museums/visited")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + accessToken)
                ).andExpect(
                        MockMvcResultMatchers.status().isOk()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[0].name").value(museumPicassoInDb.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[0].visits[0].title").value("First visit at Picasso")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].name").value(museumLeLouvreInDb.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].visits[0].title").value("First visit at Le Louvre")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].visits[1].title").value("Second visit at Le Louvre")
                );
    }

}
