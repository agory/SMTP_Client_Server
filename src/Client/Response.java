package Client;

/**
 * Created by gorya on 03/04/2017.
 */
public class Response {
    private int code = 0;
    private String message;

    public Response (String raw) {
        StringBuilder stringBuilder = new StringBuilder();
        raw.trim().chars().forEach(c -> {
            stringBuilder.append(c);
            if(c == ' ' && code == 0) {
                this.code = Integer.valueOf(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        });
        this.message = stringBuilder.toString();
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
