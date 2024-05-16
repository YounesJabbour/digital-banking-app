package ma.bank.app.service;

import ma.bank.app.Dtos.RegisterCustomerDTO;
import ma.bank.app.entity.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void saveUser(RegisterCustomerDTO registerCustomerDTO);

    Customer update(Customer user);

    void delete(Long id);

    void existsByEmail(String email);

    void changePassword(String email, String oldPassword
            , String newPassword);
}
