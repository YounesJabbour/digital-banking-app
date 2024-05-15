package ma.bank.app.web;


import lombok.RequiredArgsConstructor;
import ma.bank.app.Dtos.*;
import ma.bank.app.Dtos.BankAccountDTO;
import ma.bank.app.service.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.version}/bankAccounts")
@RequiredArgsConstructor
public class BankAccountRestController {
    private final BankAccountService bankAccountService;

    @GetMapping("")
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountService.bankAccountDTOList();
    }

    @GetMapping("/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) {
        return bankAccountService.getBankAccount(id);
    }

    @PostMapping("/savingAccount")
    public SavingBankAccountDTO saveSavingAccount(@RequestBody SavingAccountRequest request) {
        return bankAccountService.saveSavingAccount(request.getInitialBalance(), request.getCustomerId(), request.getRate());
    }

    @PostMapping("/currentAccount")
    public CurrentBankAccountDTO saveCurrentAccount(@RequestBody CurrentAccountRequest request) {
        return bankAccountService.saveCurrentAccount(request.getInitialBalance(), request.getCustomerId(), request.getOverdraft());
    }

    @GetMapping("/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/{accountId}/pageOperation")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "4") int size) {
        System.out.println("account" + accountId);
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/debit")
    public ResponseEntity<?> debit(@RequestBody DebitDTO request) {
        bankAccountService.debit(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credit")
    public ResponseEntity<?> credit(@RequestBody CreditDTO credit) {
        bankAccountService.credit(credit);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferDTO transferDTO) {
        bankAccountService.transfer(transferDTO.getBankAccountSourceId(), transferDTO.getBankAccountDestinationId(), transferDTO.getAmount());
    }

}
