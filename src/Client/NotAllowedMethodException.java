package Client;

/**
 * Created by gorya on 03/04/2017.
 */
public class NotAllowedMethodException extends SMTPException {
    public NotAllowedMethodException() {
    }

    public NotAllowedMethodException(String message) {
        super(message);
    }

    public NotAllowedMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowedMethodException(Throwable cause) {
        super(cause);
    }

    public NotAllowedMethodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
