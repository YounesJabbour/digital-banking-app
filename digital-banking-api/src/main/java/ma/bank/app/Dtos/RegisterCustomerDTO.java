package ma.bank.app.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerDTO {
    private String name;
    @Email
    private String email;
    @Size(min = 8, message = "password should have at least 8 characters")
    private String password;
}
