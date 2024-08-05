package com.xingcdev.museum.domain.dto;

import com.xingcdev.museum.domain.entities.Visit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    private UUID id;

    private String username;

    private String password;

    private List<Visit> visits;
}
