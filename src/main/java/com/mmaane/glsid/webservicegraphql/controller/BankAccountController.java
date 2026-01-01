package com.mmaane.glsid.webservicegraphql.controller;

import com.mmaane.glsid.webservicegraphql.dto.ReceiveAccount;
import com.mmaane.glsid.webservicegraphql.dto.RequestAccount;
import com.mmaane.glsid.webservicegraphql.service.BankAccountService;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public List<ReceiveAccount> getAllAccounts() {
        return bankAccountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ReceiveAccount getAccountById(@PathVariable Long id) {
        return bankAccountService.getAccountById(id);
    }

    @PostMapping
    public ReceiveAccount addAccount(@RequestBody RequestAccount requestAccount) {
        return bankAccountService.addAccount(requestAccount)
                .orElseThrow(() -> new RuntimeException("Failed to save account"));
    }

    @DeleteMapping("/{id}")
    public boolean deleteAccount(@PathVariable Long id) {
        return bankAccountService.deleteAccount(id);
    }
}