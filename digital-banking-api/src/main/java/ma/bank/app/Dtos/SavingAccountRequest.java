package ma.bank.app.Dtos;

import lombok.Data;

@Data
public class SavingAccountRequest {
    private double initialBalance;
    private Long customerId;
    private double rate;
}
