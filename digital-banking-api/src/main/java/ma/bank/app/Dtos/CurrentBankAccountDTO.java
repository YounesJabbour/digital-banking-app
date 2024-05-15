package ma.bank.app.Dtos;


import lombok.Data;
import ma.bank.app.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccountDTO
        extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overdraft;
}
