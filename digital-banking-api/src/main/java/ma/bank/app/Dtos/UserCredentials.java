package ma.bank.app.Dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserCredentials {
    private String email;
    private String password;
}
