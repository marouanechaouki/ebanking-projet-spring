package fr.cnam.ebankingbackend.exceptions;

public class BankAccountNotFoundException extends Throwable {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
