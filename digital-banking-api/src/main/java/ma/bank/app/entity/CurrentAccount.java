package ma.bank.app.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("CA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentAccount extends BankAccount {
    private double overDraft;
}
