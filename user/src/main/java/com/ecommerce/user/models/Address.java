package com.ecommerce.user.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data


public class Address {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zip;

}
