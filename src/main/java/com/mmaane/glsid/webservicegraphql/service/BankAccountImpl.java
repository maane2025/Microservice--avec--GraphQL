package com.mmaane.glsid.webservicegraphql.service;


import com.mmaane.glsid.webservicegraphql.dto.ReceiveAccount;
import com.mmaane.glsid.webservicegraphql.dto.RequestAccount;
import com.mmaane.glsid.webservicegraphql.entity.BankAccount;
import com.mmaane.glsid.webservicegraphql.mapper.BankAccountMapper;
import com.mmaane.glsid.webservicegraphql.repository.BankAccountRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountImpl implements BankAccountService {


    BankAccountRepo bankAccountRepo;
    BankAccountMapper bankAccountMapper;

    public BankAccountImpl(BankAccountRepo bankAccountRepo, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepo = bankAccountRepo;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public Optional<ReceiveAccount> addAccount(RequestAccount requestAccount) {
        BankAccount bankAccount = bankAccountMapper.toEntity(requestAccount);
        bankAccountRepo.save(bankAccount);
        ReceiveAccount receiveAccount = bankAccountMapper.toDto(bankAccount);
        return Optional.ofNullable(receiveAccount);
    }

    @Override
    public boolean deleteAccount(Long id) {
        if (bankAccountRepo.existsById(id)) {
            bankAccountRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ReceiveAccount updateAccount(Long id, RequestAccount requestAccount) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepo.findById(id);
        if (bankAccountOptional.isPresent()) {
            BankAccount bankAccount = bankAccountOptional.get();
            bankAccount.setName(requestAccount.getName());
            bankAccount.setNumber(requestAccount.getNumber());
            bankAccount.setOwner(requestAccount.getOwner());
            bankAccountRepo.save(bankAccount);
            return bankAccountMapper.toDto(bankAccount);
        }
        return null;
    }

    @Override
    public List<ReceiveAccount> getAllAccounts() {
        return bankAccountRepo.findAll()
                .stream()
                .map(bankAccountMapper::toDto)
                .toList();
    }

    @Override
    public ReceiveAccount getAccountById(Long id) {
        return bankAccountRepo.findById(id)
                .map(bankAccountMapper::toDto)
                .orElse(null);
    }
}