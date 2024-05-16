package ma.bank.app.service.impl;

import lombok.RequiredArgsConstructor;
import ma.bank.app.Dtos.RegisterCustomerDTO;
import ma.bank.app.Repository.CustomerRepository;
import ma.bank.app.entity.Customer;
import ma.bank.app.enums.Roles;
import ma.bank.app.exception.BadRequestException;
import ma.bank.app.exception.CustomerAlreadyExistException;
import ma.bank.app.exception.CustomerNotFoundException;
import ma.bank.app.mappers.interfaces.UserMapper;
import ma.bank.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findCustomerByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
    }

    @Override
    public void saveUser(RegisterCustomerDTO registerCustomerDTO) {
        System.out.println(registerCustomerDTO.getEmail());
        Customer customer = customerRepository.findCustomerByEmail(registerCustomerDTO.getEmail()).orElse(null);
        if (customer != null) {
            throw new CustomerAlreadyExistException("Customer already exists");
        }
        Customer customer1 = userMapper.toCustomer(registerCustomerDTO);
        customer1.setRole(Roles.USER);
        customer1.setPassword(passwordEncoder.encode(registerCustomerDTO.getPassword()));
        customerRepository.save(customer1);
    }

    @Override
    public Customer update(Customer user) {
        return customerRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void existsByEmail(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already taken!");
        }
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        if (!passwordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect!");
        }
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }
}
