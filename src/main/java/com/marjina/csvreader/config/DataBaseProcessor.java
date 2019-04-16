package com.marjina.csvreader.config;

import com.marjina.csvreader.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
public class DataBaseProcessor implements ItemProcessor<Customer, Customer> {


    @Override
    public Customer process(Customer customer){
        System.out.println(customer.toString());
        if(customer.isComplete()) {
            return customer;
        }
        else{
            return null;
        }
    }
}
