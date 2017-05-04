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
            for (Mail mail : mails) {
                System.out.println("Start to send mail : " + mail.getFrom() + " => " + mail.getTo());
                try {
                    clientSMTP.mail(mail.getFrom());
                    for (String to : mail.getTo()) {
                        clientSMTP.rcpt(to);
                    }
                    clientSMTP.data(mail.toString());
                } catch (UnknowException e) {
                    System.out.println("Unknown destination : " + mail.getTo());
                } catch (SMTPException e) {
                    System.out.println(e.getClass()+" : "+e.getMessage());
                } finally {
                    clientSMTP.rset();
                }
            }
            clientSMTP.quit();
        } catch (Exception e) {
            System.out.println(e.getClass()+" : "+e.getMessage());
            try {
                clientSMTP.quit();
            } catch (Exception e1) {
                System.out.println(e1.getClass()+" : "+e1.getMessage());
            }
        }
    }


}
