package ma.bank.app.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("SA")
@Setter
@Getter
public class SavingAccount extends BankAccount {
    private double interestRate;
}
