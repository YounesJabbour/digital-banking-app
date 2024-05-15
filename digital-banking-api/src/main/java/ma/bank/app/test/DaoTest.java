package ma.bank.app.test;

import ma.bank.app.AppApplication;
import ma.bank.app.Dtos.CustomerDTO;
import ma.bank.app.Repository.AccountOperationRepository;
import ma.bank.app.Repository.BankAccountRepository;
import ma.bank.app.Repository.CustomerRepository;
import ma.bank.app.entity.AccountOperation;
import ma.bank.app.entity.CurrentAccount;
import ma.bank.app.entity.Customer;
import ma.bank.app.entity.SavingAccount;
import ma.bank.app.enums.AccountStatus;
import ma.bank.app.enums.OperationType;
import ma.bank.app.service.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DaoTest {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountService bankAccountService, BankAccountRepository bankAccountRepository, CustomerRepository customerRepository) {
        return args -> {
            Stream.of("Hassan", "Mohamed", "Ali", "Omar", "abdlillah").forEach(name -> {
                CustomerDTO customer = CustomerDTO.builder().name(name).email(name + "@gmail.com").build();
                bankAccountService.saveCustomer(customer);
            });
            /**
             * create a current account
             */
            CurrentAccount ca = new CurrentAccount();
            ca.setOverDraft(1000);
            ca.setBalance(2000.0);
            ca.setCustomer(customerRepository.findById(1L).get());
            ca.setStatus(AccountStatus.ACTIVATED);
            ca.setCurrency("MAD");
            bankAccountRepository.save(ca);

            /**
             * create a saving account
             */
            SavingAccount sa = new SavingAccount();
            sa.setBalance(2000.0);
            sa.setCustomer(customerRepository.findById(1L).get());
            sa.setStatus(AccountStatus.SUSPENDED);
            sa.setCurrency("MAD");
//            sa.setCreatedAt(new Date());
            sa.setInterestRate(3.5);
            bankAccountRepository.save(sa);
        };
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
                            AccountOperationRepository accountOperationRepository,
                            BankAccountRepository bankAccountRepository){
        return args -> {
            Stream.of("hamid","taha","mohammed").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@email.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
//                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
//                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i <5 ; i++) {
                    AccountOperation accountOperation = new AccountOperation();
//                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setOperationType(Math.random()>=0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }
}
