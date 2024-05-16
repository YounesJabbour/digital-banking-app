package ma.bank.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.bank.app.Dtos.*;
import ma.bank.app.Repository.AccountOperationRepository;
import ma.bank.app.Repository.BankAccountRepository;
import ma.bank.app.Repository.CustomerRepository;
import ma.bank.app.entity.*;
import ma.bank.app.exception.BalanceNotSufficientException;
import ma.bank.app.exception.BankAccountNotFoundException;
import ma.bank.app.exception.CustomerNotFoundException;
import ma.bank.app.mappers.impl.BankAccountMapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public List<CustomerDTO> searchCustomer(String keyword) {
        List<Customer> customers = customerRepository.findByNameContains(keyword);
        return customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).toList();
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        log.info("Saving customer");
        Customer customer1 = customerRepository.save(dtoMapper.fromCustomerDTO(customer));
        return dtoMapper.fromCustomer(customer1);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentAccount(double initialBalance, Long customerId, double overdraft) {
        Customer customer1 = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setCustomer(customer1);
        return dtoMapper.fromCurrentAccount(bankAccountRepository.save(currentAccount));
    }

    @Override
    public SavingBankAccountDTO saveSavingAccount(double initialBalance, Long customerId, double rate) {
        Customer customer1 = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        SavingAccount sa = new SavingAccount();
        sa.setBalance(initialBalance);
        sa.setInterestRate(rate);
        sa.setCustomer(customer1);
        return dtoMapper.fromSavingAccount(bankAccountRepository.save(sa));

    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .toList();
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(BankAccountNotFoundException::new);
        if (bankAccount instanceof CurrentAccount) {
            return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount);
        } else {
            return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);
        }
    }

    @Override
    public void debit(DebitDTO debitDTO) {

        BankAccount bankAccount = this.bankAccountRepository.findById(debitDTO.getAccountId()).orElseThrow(BankAccountNotFoundException::new);
        if (bankAccount.getBalance() < debitDTO.getAmount())
            throw new BalanceNotSufficientException();

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(debitDTO.getAmount());
        accountOperation.setDescription(debitDTO.getDescription());
        accountOperation.setBankAccount(bankAccount);

        bankAccount.setBalance(bankAccount.getBalance() - debitDTO.getAmount());
        this.bankAccountRepository.save(bankAccount);
        this.accountOperationRepository.save(accountOperation);
    }

    @Override
    public void credit(CreditDTO creditDTO) {
        BankAccount bankAccount = this.bankAccountRepository.findById(creditDTO.getAccountId()).orElseThrow(BankAccountNotFoundException::new);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(creditDTO.getAmount());
        accountOperation.setDescription(creditDTO.getDescription());
        accountOperation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance() + creditDTO.getAmount());
        this.bankAccountRepository.save(bankAccount);
        this.accountOperationRepository.save(accountOperation);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficientException, BankAccountNotFoundException {
        DebitDTO debitDTO = DebitDTO.builder().accountId(accountIdSource).amount(amount).description("Transfer to " + accountIdDestination).build();
        CreditDTO creditDTO = CreditDTO.builder().accountId(accountIdDestination).amount(amount).description("Transfer from " + accountIdSource).build();
        this.debit(debitDTO);
        this.credit(creditDTO);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        return dtoMapper.fromCustomer(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<BankAccountDTO> bankAccountDTOList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream()
                .map(bankAccount -> {
                    if (bankAccount instanceof CurrentAccount) {
                        return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount);
                    } else {
                        return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);
                    }
                })
                .toList();
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        System.out.println("accountId = " + accountId);
        try {
            List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_Id(accountId);
            return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BankAccountNotFoundException();
        }
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(BankAccountNotFoundException::new);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_Id(accountId, PageRequest.of(page, size));
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).toList();
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public Map<String, String> login(String email, String password) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));

            Customer customer = (Customer) authentication.getPrincipal();
            Instant instant = Instant.now();
//        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .expiresAt(instant.plus(20, ChronoUnit.MINUTES))
                    .subject(customer.getEmail())
                    .claim("scope", customer.getRole().name())
                    .claim("name", customer.getName())
                    .build();
            JwtEncoderParameters jwtEncoderParameters =
                    JwtEncoderParameters.from(
                            JwsHeader.with(MacAlgorithm.HS512).build(),
                            jwtClaimsSet
                    );
            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return Map.of("error", "Authentication failed");
        }

    }
}
