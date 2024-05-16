package ma.bank.app.Dtos;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PasswordChangeRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
