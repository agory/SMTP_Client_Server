package client.exception;

/**
 * Created by gorya on 09/04/2017.
 */
public class UnknowException extends SMTPException {
    public UnknowException() {
    }

    public UnknowException(String message) {
        super(message);
    }

    public UnknowException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknowException(Throwable cause) {
        super(cause);
    }

    public UnknowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
