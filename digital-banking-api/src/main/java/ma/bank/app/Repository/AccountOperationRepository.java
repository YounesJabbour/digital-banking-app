package ma.bank.app.Repository;

import ma.bank.app.entity.AccountOperation;
import ma.bank.app.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AccountOperationRepository extends
        JpaRepository<AccountOperation, Long> {

    Page<AccountOperation> findByBankAccount_Id(String id, Pageable pageable);


    List<AccountOperation> findByBankAccount_Id(String accountId);
}
