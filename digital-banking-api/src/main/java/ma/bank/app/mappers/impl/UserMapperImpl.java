package ma.bank.app.mappers.impl;

import ma.bank.app.Dtos.CustomerDTO;
import ma.bank.app.Dtos.RegisterCustomerDTO;
import ma.bank.app.entity.Customer;
import ma.bank.app.mappers.interfaces.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class UserMapperImpl implements UserMapper {
    @Override
    public Customer toCustomer(RegisterCustomerDTO registerCustomerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(registerCustomerDTO, customer);
        return customer;
    }

    @Override
    public RegisterCustomerDTO toRegisterCustomerDTO(Customer customer) {
        return null;
    }

    @Override
    public CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    @Override
    public Customer toCustomerDTO(CustomerDTO customerDTO) {
        return null;
    }
}
