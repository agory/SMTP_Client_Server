package Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorya on 4/23/17.
 */
public class Main {
    public static void main(String[] args) {
        MailSender mailSender = MailSender.getInstance();
        List<Mail> mails = new ArrayList<>();
        mails.add(new Mail()
                .setFrom("toto@toto.fr")
                .setTo("toto2@toto.fr")
                .setSubject("toto")
                .setDate(new Date())
                .setContent("tagada tada gadadada")
        );
        mailSender.send(mails);
    }
}
