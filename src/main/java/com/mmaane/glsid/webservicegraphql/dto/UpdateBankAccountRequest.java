package com.mmaane.glsid.webservicegraphql.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBankAccountRequest {

    private String ownerName;
    private String currency;
}