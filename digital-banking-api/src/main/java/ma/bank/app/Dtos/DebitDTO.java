package ma.bank.app.Dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DebitDTO {
    private String accountId;
    private double amount;
    private String description;
}
