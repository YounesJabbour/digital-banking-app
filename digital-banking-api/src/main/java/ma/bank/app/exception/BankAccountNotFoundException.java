package ma.bank.app.exception;


public class BankAccountNotFoundException extends NotFoundException {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
    public BankAccountNotFoundException() {
        super("Bank account not found.");
    }
}
