package ma.bank.app.Repository;

import ma.bank.app.entity.BankAccount;
import ma.bank.app.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContains(String keyword);
}
