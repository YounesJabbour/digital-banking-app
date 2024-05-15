package ma.bank.app.service;

import ma.bank.app.Dtos.*;

import java.util.List;
import java.util.Map;

public interface BankAccountService {

    List<CustomerDTO> searchCustomer(String keyword);

    CustomerDTO saveCustomer(CustomerDTO customer);

    CurrentBankAccountDTO saveCurrentAccount(double initialBalance, Long customerId, double overdraft);

    SavingBankAccountDTO saveSavingAccount(double initialBalance, Long customerId, double rate);

    List<CustomerDTO> listCustomers();

    BankAccountDTO getBankAccount(String accountId);

    void debit(DebitDTO debitDTO);

    void credit(CreditDTO creditDTO);

    void transfer(String accountIdSource, String accountIdDestination, double amount);

    CustomerDTO getCustomer(Long customerId);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    List<BankAccountDTO> bankAccountDTOList();

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size);

    public Map<String, String> login(String username, String password);
}
