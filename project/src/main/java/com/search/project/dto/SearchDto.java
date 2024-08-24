package com.search.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchDto {
    @NotBlank
    private String location;
    @NotBlank
    private String natureOfBusiness;
    @NotBlank
    private String manufacturingProcesses;
    @Min(1)
    private int limit;
    @Min(0)
    private int page;
}
