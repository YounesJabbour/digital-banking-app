package ma.bank.app.mappers.interfaces;

import ma.bank.app.Dtos.CustomerDTO;
import ma.bank.app.Dtos.RegisterCustomerDTO;
import ma.bank.app.entity.Customer;

public interface UserMapper {

    public Customer toCustomer(RegisterCustomerDTO registerCustomerDTO);

    public RegisterCustomerDTO toRegisterCustomerDTO(Customer customer);

    public CustomerDTO toCustomerDTO(Customer customer);

    public Customer toCustomerDTO(CustomerDTO customerDTO);
}
