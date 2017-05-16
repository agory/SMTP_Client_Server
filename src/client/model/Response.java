package client.model;

/**
 * Created by gorya on 03/04/2017.
 */
public class Response {
    private int code = 0;
    private String message;

    public Response (String raw) {
        String[] params = raw.split(" ");
        code =Integer.valueOf(params[0]);
        message = raw;

    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
