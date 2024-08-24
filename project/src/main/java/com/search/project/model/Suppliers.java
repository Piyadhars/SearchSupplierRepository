package com.search.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "suppliers")
@NoArgsConstructor
public class Suppliers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @NotBlank
    @Size(max = 255)
    private String companyName;

    @Size(max = 255)
    private String website;

    @NotBlank
    @Size(max = 100)
    private String location;

    @NotNull
    @Pattern(regexp = "small_scale|medium_scale|large_scale")
    private String natureOfBusiness;

    @NotNull
    @Pattern(regexp = "moulding|3d_printing|casting|coating")
    private String manufacturingProcesses;

    public Suppliers(long l, String s, String url, String india, String smallScale, String s1) {
        this.supplierId = l;
        this.companyName = s;
        this.website = url;
        this.location = india;
        this.natureOfBusiness = smallScale;
        this.manufacturingProcesses = s1;
    }

}
