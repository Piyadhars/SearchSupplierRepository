package com.search.project.repository;


import com.search.project.model.Suppliers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<Suppliers,  Long> {

    List<Suppliers> findAll();

    List<Suppliers> findByLocationAndNatureOfBusinessAndManufacturingProcesses(
            String location,
            String natureOfBusiness,
            String manufacturingProcesses
    );

    Page<Suppliers> findByLocationAndNatureOfBusinessAndManufacturingProcesses(
            String location,
            String natureOfBusiness,
            String manufacturingProcesses,
            Pageable pageable
    );

}
