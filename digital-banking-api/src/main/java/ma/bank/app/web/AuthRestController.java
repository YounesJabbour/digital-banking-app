package ma.bank.app.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.bank.app.Dtos.PasswordChangeRequest;
import ma.bank.app.Dtos.RegisterCustomerDTO;
import ma.bank.app.Dtos.UserCredentials;
import ma.bank.app.service.BankAccountService;
import ma.bank.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}/auth")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("permitAll()")
public class AuthRestController {

    private final BankAccountService bankAccountService;
    private final UserService userService;


    //    @PreAuthorize("permitAll()")
    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserCredentials userCredentials) {
        log.info("Logging in user: {}", userCredentials.getEmail());
        log.info("Logging in user: {}", userCredentials.getPassword());
        return ResponseEntity.ok(bankAccountService.login(userCredentials.getEmail(), userCredentials.getPassword()));
    }

    //    @PreAuthorize("permitAll()")
    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<Void> register(@RequestBody RegisterCustomerDTO registerCustomerDTO) {
        log.info("Registering user: {}", registerCustomerDTO);
        userService.saveUser(registerCustomerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/changePassword", consumes = "application/json")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(
                passwordChangeRequest.getEmail(),
                passwordChangeRequest.getOldPassword(),
                passwordChangeRequest.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
}

