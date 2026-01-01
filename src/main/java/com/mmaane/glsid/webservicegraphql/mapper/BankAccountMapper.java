package com.mmaane.glsid.webservicegraphql.mapper;

import com.mmaane.glsid.webservicegraphql.dto.*;
import com.mmaane.glsid.webservicegraphql.entity.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;

@Component
public class BankAccountMapper {

    public BankAccount toEntity(RequestAccount requestAccount) {
        return BankAccount.builder()
                .name(requestAccount.getName())
                .number(requestAccount.getNumber())
                .owner(requestAccount.getOwner())
                .build();
    }
    public ReceiveAccount toDto(BankAccount bankAccount) {
        return new ReceiveAccount(
                bankAccount.getId(),
                bankAccount.getName(),
                bankAccount.getNumber(),
                bankAccount.getOwner()
        );
    }
}