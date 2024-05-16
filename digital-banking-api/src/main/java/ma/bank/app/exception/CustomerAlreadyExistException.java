package ma.bank.app.exception;

import org.springframework.http.HttpStatus;

public class CustomerAlreadyExistException extends ApiException {
    public CustomerAlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public CustomerAlreadyExistException() {
        super("Email already exist.", HttpStatus.CONFLICT);
    }
}

