package ma.bank.app.Repository;

import ma.bank.app.entity.BankAccount;
import ma.bank.app.entity.Customer;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContains(String keyword);

    Boolean existsByEmail(String email);

    Optional<Customer> findCustomerByEmail(String email);
}
