package com.search.project.controller;


import com.search.project.dto.SearchDto;
import com.search.project.model.Suppliers;
import com.search.project.service.SupplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class SupplyController  {

    private final SupplyService supplyService;

    @Operation(summary = "Search for suppliers", description = "Retrieve a list of suppliers based on location, nature of business, and manufacturing process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Suppliers.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No suppliers found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/api/supplier/query")
    public ResponseEntity<List<Suppliers>> searchSupplier(@Valid @RequestBody SearchDto searchDto){
        return supplyService.getSuppliersByCriteria(searchDto);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @Operation(summary = "Handle ResponseStatusException", description = "Handle exceptions where a specific response status is required")
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    @Operation(summary = "Handle Generic Exception", description = "Handle any unexpected exceptions")
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
