package com.search.project.service;

import com.search.project.dto.SearchDto;
import com.search.project.model.Suppliers;
import com.search.project.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SupplyService {

    private final SupplyRepository supplyRepository;

    public ResponseEntity<List<Suppliers>> getSuppliersByCriteria(SearchDto searchDto) {
        if (!Pattern.matches("small_scale|medium_scale|large_scale", searchDto.getNatureOfBusiness())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid nature of business");
        }
        if (!Pattern.matches("moulding|3d_printing|casting|coating", searchDto.getManufacturingProcesses())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid manufacturing process");
        }
        Page<Suppliers> suppliers = supplyRepository.findByLocationAndNatureOfBusinessAndManufacturingProcesses(searchDto.getLocation(), searchDto.getNatureOfBusiness(), searchDto.getManufacturingProcesses(), PageRequest.of(searchDto.getPage(),searchDto.getLimit()));

        if (suppliers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No suppliers found for the given criteria");
        }
        return ResponseEntity.ok(suppliers.getContent());
    }
}