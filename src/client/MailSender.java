package client;

import client.exception.NotAllowedMethodException;
import client.exception.RequestFailedException;
import client.exception.SMTPException;
import client.exception.UnknowException;
import client.model.ClientSMTP;
import client.model.Mail;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(Config.SMTPHost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ClientSMTP clientSMTP = new ClientSMTP(ip, Config.port);

        try {
            clientSMTP.run();
            clientSMTP.ehlo(Config.domain);
            mails.forEach(mail -> {
                System.out.println("send mail : " + mail.getFrom() + " => " + mail.getTo());
                try {
                    clientSMTP.mail(mail.getFrom());
                    clientSMTP.rcpt(mail.getTo());
                    clientSMTP.data(mail.toString());
                    clientSMTP.rset();
                } catch (UnknowException e) {
                    System.out.println("Unknown destination : " + mail.getTo());
                    try {
                        clientSMTP.rset();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                } catch (SMTPException e) {
                    e.printStackTrace();
                }
            });
            clientSMTP.quit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                clientSMTP.quit();
            } catch (NotAllowedMethodException e1) {
                e1.printStackTrace();
            } catch (RequestFailedException e1) {
                e1.printStackTrace();
            }
        }
    }


}
