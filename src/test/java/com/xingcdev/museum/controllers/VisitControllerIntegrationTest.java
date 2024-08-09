package com.xingcdev.museum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xingcdev.museum.domain.dto.VisitRequestBody;
import com.xingcdev.museum.services.MuseumService;
import com.xingcdev.museum.services.VisitService;
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

import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class VisitControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private final static Logger logger = LoggerFactory.getLogger(VisitControllerIntegrationTest.class);

    private final VisitService visitService;
    private final MuseumService museumService;

    private static String accessToken;

    private static String currentUserId;

    @Autowired
    public VisitControllerIntegrationTest(MockMvc mockMvc, VisitService visitService, MuseumService museumService) {
        this.mockMvc = mockMvc;
        this.visitService = visitService;
        this.museumService = museumService;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeAll
    static void setup() {
        logger.info("Authenticating...");

        var authUtils = new AuthUtils();
        var result = authUtils.authenticate();
        accessToken = result.getAccessToken();
        currentUserId = result.getUserId();
    }

    @Test
    public void getVisitsShouldReturnListOfVisits() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());
        visitService.save(TestDataUtil.createVisitA(currentUserId, museumInDb));
        visitService.save(TestDataUtil.createVisitB(currentUserId, museumInDb));
        visitService.save(TestDataUtil.createVisitC(currentUserId, museumInDb));

        mockMvc.perform(MockMvcRequestBuilders.get("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].comment").value("Comment of visit A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].comment").value("Comment of visit B"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].comment").value("Comment of visit C"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageInfo.totalResults").value(3));
    }

    @Test
    public void getVisitShouldReturnVisitWhenExist() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());
        var visitInDb = visitService.save(TestDataUtil.createVisitA(currentUserId, museumInDb));

        mockMvc.perform(MockMvcRequestBuilders.get("/visits/" + visitInDb.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(visitInDb.getId().toString())
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.comment").value("Comment of visit A")
                );
    }

    @Test
    public void getVisitShouldReturn404WhenNoExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/visits/00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.code").value("visitNotFound")
                );
    }

    @Test
    public void createVisitShouldReturnCreatedVisit() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());

        var visitDto = VisitRequestBody
                .builder()
                .comment("Comment of VisitRequestBody A")
                .museumId(museumInDb.getId())
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.comment").value("Comment of VisitRequestBody A")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.museum.id").value(museumInDb.getId().toString())
        );
    }

    @Test
    public void createVisitShouldReturnMuseumNotFound() throws Exception {
        var visitDto = VisitRequestBody
                .builder()
                .comment("Comment of VisitRequestBody")
                .museumId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("museumNotFound")
        );
    }

    @Test
    public void createVisitShouldReturnDuplicateMuseumError() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());

        var visitDto = VisitRequestBody
                .builder()
                .comment("Comment of VisitRequestBody")
                .museumId(museumInDb.getId())
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("duplicateMuseum")
        );
    }

    @Test
    public void fullUpdateVisitShouldReturnUpdatedVisit() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());
        var visitInDb = visitService.save(TestDataUtil.createVisitA(currentUserId, museumInDb));

        var visitDto = VisitRequestBody
                .builder()
                .comment("Comment updated")
                .museumId(visitInDb.getMuseum().getId())
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/visits/" + visitInDb.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(visitInDb.getId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.comment").value("Comment updated")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.museum.id").value(museumInDb.getId().toString())
        );
    }

    @Test
    public void fullUpdateVisitShouldReturn404WhenNoVisitExists() throws Exception {
        var visitDto = VisitRequestBody
                .builder()
                .comment("Comment updated")
                .museumId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/visits/00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
                )
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.code").value("visitNotFound")
                );
    }

    @Test
    public void partialUpdateVisitShouldReturnUpdatedVisit() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());
        var visitInDb = visitService.save(TestDataUtil.createVisitA(currentUserId, museumInDb));

        var visitDto = VisitRequestBody
                .builder()
                .comment("Comment updated")
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/visits/" + visitInDb.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(visitInDb.getId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.comment").value("Comment updated")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.museum.id").value(museumInDb.getId().toString())
        );
    }

    @Test
    public void partialUpdateVisitShouldReturnMuseumNotFound() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());
        var visitInDb = visitService.save(TestDataUtil.createVisitA(currentUserId, museumInDb));

        var visitDto = VisitRequestBody
                .builder()
                .museumId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/visits/" + visitInDb.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("museumNotFound")
        );
    }

    @Test
    public void deleteVisitShouldReturnDeletedVisit() throws Exception {
        var museumInDb = museumService.save(TestDataUtil.createMuseumLeLouvre());
        var visitInDb = visitService.save(TestDataUtil.createVisitA(currentUserId, museumInDb));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/visits/" + visitInDb.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(visitInDb.getId().toString())
        );
    }
}
