package Client;

/**
 * Created by gorya on 09/04/2017.
 */
public abstract class SMTPException extends Exception  {
    public SMTPException() {
    }

    public SMTPException(String message) {
        super(message);
    }

    public SMTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMTPException(Throwable cause) {
        super(cause);
    }

    public SMTPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}