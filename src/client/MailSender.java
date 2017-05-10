package client;

import client.exception.NotAllowedMethodException;
import client.exception.RequestFailedException;
import client.exception.SMTPException;
import client.exception.UnknowException;
import client.model.ClientSMTP;
import client.model.Mail;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

    public void send(List<Mail> mails) throws UnknowException, NotAllowedMethodException, RequestFailedException {
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(Config.SMTPHost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ClientSMTP clientSMTP = new ClientSMTP(ip, Config.port);
        List<String> unkownMail = new ArrayList<>();
        int messageSend = 0;
        int messageTotal = 0;

        try {
            clientSMTP.run();
            clientSMTP.ready();
            clientSMTP.ehlo(Config.domain);
            for (Mail mail : mails) {
                System.out.println("Start to send mail : " + mail.getFrom() + " => " + mail.getTo());
                clientSMTP.mail(mail.getFrom());
                int i = 0;
                for (String to : mail.getTo()) {
                    messageTotal++;
                    try {
                        clientSMTP.rcpt(to);
                        i++;
                    } catch (UnknowException e) {
                        unkownMail.add(to);
                    }
                }
                messageSend+= i;
                if(i> 0)
                    clientSMTP.data(mail.toString());
                clientSMTP.rset();
            }
            clientSMTP.quit();
            if(unkownMail.size() > 0) {
                StringBuilder builder = new StringBuilder();
                unkownMail.forEach(mail -> builder.append(mail + ","));
                throw new UnknowException(messageSend + " / " + messageTotal + " message send \n unknown User " + builder.toString());
            }
        }  catch (UnknowException e) {
           throw e;
        } catch (SMTPException e) {
            System.out.println(e.getClass() + " : " + e.getMessage());
            try {
                clientSMTP.rset();
                clientSMTP.quit();
            } catch (Exception e1) {
                System.out.println(e1.getClass() + " : " + e1.getMessage());
            }
            throw e;
        }
    }


}
