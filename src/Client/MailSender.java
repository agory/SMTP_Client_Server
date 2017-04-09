package Client;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by gorya on 09/04/2017.
 */
public class MailSender {
    private static MailSender instance;

    private MailSender() {
    }

    public static MailSender getInstance() {
        if (instance == null)
            instance = new MailSender();
        return instance;
    }

    public void send(List<Mail> mails) {
        try {
            InetAddress ip = InetAddress.getByName(Config.SMTPHost);;
            ClientSMTP clientSMTP = new ClientSMTP(ip, Config.port);
            clientSMTP.run();
            clientSMTP.ehlo(Config.domain);
            mails.forEach(mail -> {
                try {
                    clientSMTP.rcpt(mail.getTo());
                    clientSMTP.mail(mail.getFrom());
                    clientSMTP.mail(mail.toString());
                } catch (Exception e) {
                    try {
                        clientSMTP.rset();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
