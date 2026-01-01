package com.mmaane.glsid.webservicegraphql.repository;


import com.mmaane.glsid.webservicegraphql.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

}