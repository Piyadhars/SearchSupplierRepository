package com.search.project;

import com.search.project.dto.SearchDto;
import com.search.project.model.Suppliers;
import com.search.project.repository.SupplyRepository;
import com.search.project.service.SupplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SupplyServiceTest {

    @Mock
    private SupplyRepository supplyRepository;

    @InjectMocks
    private SupplyService supplyService;

    private SearchDto validSearchDto;
    private SearchDto invalidNatureOfBusinessDto;
    private SearchDto invalidManufacturingProcessDto;

    @BeforeEach
    public void setUp() {
        validSearchDto = new SearchDto();
        validSearchDto.setLocation("India");
        validSearchDto.setNatureOfBusiness("small_scale");
        validSearchDto.setManufacturingProcesses("3d_printing");
        validSearchDto.setPage(0);
        validSearchDto.setLimit(10);

        invalidNatureOfBusinessDto = new SearchDto();
        invalidNatureOfBusinessDto.setLocation("India");
        invalidNatureOfBusinessDto.setNatureOfBusiness("invalid_scale");
        invalidNatureOfBusinessDto.setManufacturingProcesses("3d_printing");
        invalidNatureOfBusinessDto.setPage(0);
        invalidNatureOfBusinessDto.setLimit(10);

        invalidManufacturingProcessDto = new SearchDto();
        invalidManufacturingProcessDto.setLocation("India");
        invalidManufacturingProcessDto.setNatureOfBusiness("small_scale");
        invalidManufacturingProcessDto.setManufacturingProcesses("invalid_process");
        invalidManufacturingProcessDto.setPage(0);
        invalidManufacturingProcessDto.setLimit(10);
    }

    @Test
    public void testGetSuppliersByCriteria_Success() {
        Suppliers supplier1 = new Suppliers(1L, "3D Makers India", "http://3dmakerindia.com", "India", "small_scale", "3d_printing");
        Suppliers supplier2 = new Suppliers(2L, "Innovative Print Co.", "http://innovativeprint.com", "India", "small_scale", "3d_printing");
        List<Suppliers> supplierList = List.of(supplier1, supplier2);
        Page<Suppliers> supplierPage = new PageImpl<>(supplierList, PageRequest.of(0, 10), supplierList.size());

        when(supplyRepository.findByLocationAndNatureOfBusinessAndManufacturingProcesses(any(), any(), any(), any()))
                .thenReturn(supplierPage);

        ResponseEntity<List<Suppliers>> response = supplyService.getSuppliersByCriteria(validSearchDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("3D Makers India", response.getBody().get(0).getCompanyName());
    }

    @Test
    public void testGetSuppliersByCriteria_InvalidNatureOfBusiness() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            supplyService.getSuppliersByCriteria(invalidNatureOfBusinessDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid nature of business", exception.getReason());
    }

    @Test
    public void testGetSuppliersByCriteria_InvalidManufacturingProcess() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            supplyService.getSuppliersByCriteria(invalidManufacturingProcessDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid manufacturing process", exception.getReason());
    }

    @Test
    public void testGetSuppliersByCriteria_NoResultsFound() {
        when(supplyRepository.findByLocationAndNatureOfBusinessAndManufacturingProcesses(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            supplyService.getSuppliersByCriteria(validSearchDto);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No suppliers found for the given criteria", exception.getReason());
    }
}

