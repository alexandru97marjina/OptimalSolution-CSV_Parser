package com.marjina.csvreader.repository;

import com.marjina.csvreader.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
