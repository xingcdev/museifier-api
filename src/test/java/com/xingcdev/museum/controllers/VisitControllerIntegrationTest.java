package com.xingcdev.museum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xingcdev.museum.domain.dto.VisitRequestBody;
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

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class VisitControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public VisitControllerIntegrationTest(MockMvc mockMvc
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void getVisitsShouldReturnListOfVisits() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/visits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].comment").value("Comment 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].comment").value("Comment 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].comment").value("Comment 3"));
    }

    @Test
    public void getVisitShouldReturnVisitWhenExist() throws Exception {
        var visitId = "b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4";
        mockMvc.perform(MockMvcRequestBuilders.get("/visits/" + visitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(visitId)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.comment").value("Comment 1")
                );
    }

    @Test
    public void getMuseumShouldReturn404WhenNoExist() throws Exception {
        var fakeId = "03e0ac58-d797-4f72-bcbc-1bff00cd24f4";
        mockMvc.perform(MockMvcRequestBuilders.get("/visits/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.code").value("visitNotFound")
                );
    }

    @Test
    public void createVisitShouldReturnCreatedVisit() throws Exception {
        var visitDto = VisitRequestBody.builder()
                .comment("Test comment")
                .accountId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc725f"))
                .museumId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc726a"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.comment").value("Test comment")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.museum.id").value("dc739b92-f0a0-47bf-a49e-0f6296dc726a")
        );
    }

    @Test
    public void createVisitShouldReturnAccountNotFound() throws Exception {
        UUID fakeAccountUUID = UUID.fromString("37f6c03d-5bbf-4f56-b1d9-e4a10000d389");

        var visitDto = VisitRequestBody.builder()
                .comment("Test comment")
                .accountId(fakeAccountUUID)
                .museumId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc726a"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("accountNotFound")
        );
    }

    @Test
    public void createVisitShouldReturnMuseumNotFound() throws Exception {
        UUID fakeMuseum = UUID.fromString("ac21d7ee-b684-4f76-80ff-0c94cef1c3ef");

        var visitDto = VisitRequestBody.builder()
                .comment("Test comment")
                .accountId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc725f"))
                .museumId(fakeMuseum)
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("museumNotFound")
        );
    }

    @Test
    public void createVisitShouldReturnDuplicateMuseumError() throws Exception {
        var visitDto = VisitRequestBody.builder()
                .comment("Test comment")
                .accountId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc725f"))
                .museumId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc726a"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("duplicateMuseum")
        );
    }

    @Test
    public void fullUpdateVisitShouldReturnUpdatedVisit() throws Exception {
        var visitId = "b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4";
        var visitDto = VisitRequestBody.builder()
                .comment("Comment updated")
                .accountId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc725e"))
                .museumId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc726b"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/visits/" + visitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(visitId)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.comment").value("Comment updated")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.museum.id").value("dc739b92-f0a0-47bf-a49e-0f6296dc726b")
        );
    }

    @Test
    public void fullUpdateVisitShouldReturn404WhenNoVisitExists() throws Exception {
        var fakeId = "03e0ac58-d797-4f72-bcbc-1bff00cd24f4";
        var visitDto = VisitRequestBody.builder()
                .comment("Comment updated")
                .accountId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc725e"))
                .museumId(UUID.fromString("dc739b92-f0a0-47bf-a49e-0f6296dc726b"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/visits/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
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
        var visitId = "b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4";
        var visitDto = VisitRequestBody.builder()
                .comment("Comment updated")
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/visits/" + visitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(visitId)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.comment").value("Comment updated")
        );
    }

    @Test
    public void partialUpdateVisitShouldNotUpdateTheId() throws Exception {
        var visitId = "b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4";
        var visitDto = VisitRequestBody.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/visits/" + visitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(visitId)
        );
    }

    @Test
    public void partialUpdateVisitShouldReturnAccountNotFound() throws Exception {
        var visitId = "b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4";
        var visitDto = VisitRequestBody.builder()
                .accountId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/visits/" + visitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("accountNotFound")
        );
    }

    @Test
    public void partialUpdateVisitShouldReturnMuseumNotFound() throws Exception {
        var visitId = "b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4";
        var visitDto = VisitRequestBody.builder()
                .museumId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .build();
        String visitDtoJson = objectMapper.writeValueAsString(visitDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/visits/" + visitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value("museumNotFound")
        );
    }

    @Test
    public void deleteVisitShouldReturnDeletedVisit() throws Exception {
        var visitId = "b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/visits/" + visitId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(visitId)
        );
    }
}
