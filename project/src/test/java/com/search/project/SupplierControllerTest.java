package com.search.project;


import com.search.project.controller.SupplyController;
import com.search.project.dto.SearchDto;
import com.search.project.model.Suppliers;
import com.search.project.service.SupplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class SupplierControllerTest {

    @InjectMocks
    private SupplyController supplierController;

    @Mock
    private SupplyService supplierService;

    @BeforeEach
    public void setUp() {
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchSupplier_Success() {
        Suppliers supplier1 = new Suppliers(1L, "3D Makers India", "http://3dmakerindia.com", "India", "small_scale", "3d_printing");
        Suppliers supplier2 = new Suppliers(2L, "Innovative Print Co.", "http://innovativeprint.com", "India", "small_scale", "3d_printing");

        List<Suppliers> suppliers = Arrays.asList(supplier1, supplier2);

        SearchDto searchDto = new SearchDto();
        searchDto.setLocation("India");
        searchDto.setNatureOfBusiness("small_scale");
        searchDto.setManufacturingProcesses("3d_printing");

        when(supplierService.getSuppliersByCriteria(searchDto)).thenReturn(new ResponseEntity<>(suppliers, HttpStatus.OK));



        ResponseEntity<List<Suppliers>> response = supplierController.searchSupplier(searchDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("3D Makers India", response.getBody().get(0).getCompanyName());
    }

    @Test
    public void testSearchSupplier_BadRequest() {
        SearchDto searchDto = new SearchDto();
        searchDto.setLocation("");
        searchDto.setNatureOfBusiness("invalid_scale");
        searchDto.setManufacturingProcesses("invalid_process");

        when(supplierService.getSuppliersByCriteria(any(SearchDto.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            supplierController.searchSupplier(searchDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid input", exception.getReason());
    }

    @Test
    public void testSearchSupplier_NoResults() {
        when(supplierService.getSuppliersByCriteria(any(SearchDto.class))).thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        SearchDto searchDto = new SearchDto();
        searchDto.setLocation("India");
        searchDto.setNatureOfBusiness("small_scale");
        searchDto.setManufacturingProcesses("3d_printing");

        ResponseEntity<List<Suppliers>> response = supplierController.searchSupplier(searchDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testSearchSupplier_InternalServerError() {
        when(supplierService.getSuppliersByCriteria(any(SearchDto.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(Exception.class, () -> {
            supplierController.searchSupplier(new SearchDto());
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testHandleResponseStatusException_BadRequest() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        ResponseEntity<String> response = supplierController.handleResponseStatusException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input", response.getBody());
    }

    @Test
    public void testHandleResponseStatusException_NotFound() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        ResponseEntity<String> response = supplierController.handleResponseStatusException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody());
    }

    @Test
    public void testHandleResponseStatusException_InternalServerError() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        ResponseEntity<String> response = supplierController.handleResponseStatusException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }


    @Test
    public void testHandleGenericException_WithMessage() {
        Exception ex = new Exception("Database connection failure");
        ResponseEntity<String> response = supplierController.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred Database connection failure", response.getBody());
    }

    @Test
    public void testHandleGenericException_WithoutMessage() {
        Exception ex = new Exception();
        ResponseEntity<String> response = supplierController.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred null", response.getBody());
    }

    @Test
    public void testHandleGenericException_NullPointerException() {
        Exception ex = new NullPointerException("Null pointer exception occurred");
        ResponseEntity<String> response = supplierController.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred Null pointer exception occurred", response.getBody());
    }

    @Test
    public void testHandleGenericException_CustomException() {
        Exception ex = new Exception("Custom exception occurred");
        ResponseEntity<String> response = supplierController.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred Custom exception occurred", response.getBody());
    }

}


