package Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorya on 3/12/17.
 */
public class Mail {

    private Date date;
    private String content;
    private Map<String, String> headers;

    public Mail() {
        this.headers = new HashMap<>();
    }

    public Mail(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int size() {
        return this.toString().getBytes().length;
    }

    public String getFrom() {
        return this.getHeader("From");
    }

    public String getTo() {
        return this.getHeader("To");
    }

    public String getSubject() {
        return this.getHeader("Subject");
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return this.getHeader("Message-ID");
    }

    public Mail setFrom(String from) {
        this.headers.put("From", from);
        return this;
    }

    public Mail setTo(String to) {
        this.addHeader("To", to);
        return this;
    }

    public Mail setSubject(String subject) {
        this.addHeader("Subject", subject);
        return this;
    }

    public Mail setId(String id) {
        this.addHeader("Message-ID", id);
        return this;
    }

    public Mail setDate(Date date) {
        this.date = date;
        return this;
    }

    public Mail setContent(String content) {
        this.content = content;
        return this;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public Mail addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;

    }

    @Override
    public String toString() {
        String mail = "";
        /*for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            mail += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }*/
        DateFormat format = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss Z");

        mail += format.format(this.date) + "\r\n";
        mail += this.getSubject() + "\r\n";
        mail += "\r\n";
        String content = this.getContent();
        for (int i = 0; i < content.length(); i += 79)
            mail += content.substring(i, (i + 78 < content.length()) ? i + 78 : content.length()) + "\r\n";
        mail += ".\r\n";
        return mail;
    }

    public String getName() {
        return this.getId().replace("<","message_").replace(">","");
    }
}
