package ma.bank.app.Dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditDTO {
    private String accountId;
    private double amount;
    private String description;
}
