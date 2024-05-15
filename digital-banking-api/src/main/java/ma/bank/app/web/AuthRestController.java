package ma.bank.app.web;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.bank.app.security.JwtConfig;
import ma.bank.app.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}/auth")
@CrossOrigin(origins = "http://localhost:4200")

public class AuthRestController {

    private final BankAccountService bankAccountService;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(String username, String password) {
        System.out.println(username + " " + password);
        return ResponseEntity.ok(bankAccountService.login(username, password));
    }

}

