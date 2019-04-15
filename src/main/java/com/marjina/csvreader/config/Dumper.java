package com.marjina.csvreader.config;

import com.marjina.csvreader.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class Dumper implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        System.out.println(customer.toString());
        if(customer.isComplete()) {
            return null;
        }
        else{
            return customer;
        }
    }
}
