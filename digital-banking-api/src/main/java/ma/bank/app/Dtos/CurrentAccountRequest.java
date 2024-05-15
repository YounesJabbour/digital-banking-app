package ma.bank.app.Dtos;


import lombok.Data;

@Data
public class CurrentAccountRequest {
    private double initialBalance;
    private Long customerId;
    private double overdraft;
}
