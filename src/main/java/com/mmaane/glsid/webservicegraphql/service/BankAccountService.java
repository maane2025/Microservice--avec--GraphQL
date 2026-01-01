package com.mmaane.glsid.webservicegraphql.service;


import com.mmaane.glsid.webservicegraphql.dto.ReceiveAccount;
import com.mmaane.glsid.webservicegraphql.dto.RequestAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountService {

    Optional<ReceiveAccount> addAccount(RequestAccount requestAccount);

    boolean deleteAccount(Long id);

    ReceiveAccount updateAccount(Long id, RequestAccount requestAccount);

    List<ReceiveAccount> getAllAccounts();

    ReceiveAccount getAccountById(Long id);
}