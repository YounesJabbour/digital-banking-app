package ma.bank.app.Dtos;

import lombok.Data;

@Data
public class TransferDTO {
    private String bankAccountSourceId;
    private double amount;
    private String description;
    private String bankAccountDestinationId;
}
