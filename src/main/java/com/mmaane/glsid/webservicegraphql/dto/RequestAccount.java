package com.mmaane.glsid.webservicegraphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccount {

    private Long id;
    private String name;
    private String number;
    private String owner;
}