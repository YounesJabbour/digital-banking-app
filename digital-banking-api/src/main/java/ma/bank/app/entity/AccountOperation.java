package ma.bank.app.entity;


import jakarta.persistence.*;
import lombok.*;
import ma.bank.app.enums.OperationType;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    @ManyToOne
    private BankAccount bankAccount;
    private String description;

    @PrePersist
    public void setDate() {
        this.operationDate = new Date();
    }
}
