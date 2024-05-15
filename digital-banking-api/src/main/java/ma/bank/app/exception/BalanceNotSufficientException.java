package ma.bank.app.exception;

public class BalanceNotSufficientException extends BadRequestException {
    public BalanceNotSufficientException(String message) {
        super(message);
    }

    public BalanceNotSufficientException() {
        super("Balance not sufficient");
    }
}