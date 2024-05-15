package ma.bank.app.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.bank.app.enums.AccountStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @Id
    private String id;
    private Double balance;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private LocalDate CreatedAt;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> operations;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
        this.CreatedAt = LocalDate.now();
    }
}

