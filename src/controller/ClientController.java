package controller;

import client.MailSender;
import client.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorya on 24/04/2017.
 */
public class ClientController {
    @FXML
    private TextField tf_from;
    @FXML
    private TextField tf_to;
    @FXML
    private TextField tf_subject;
    @FXML
    private TextArea ta_content;


    public void send(MouseEvent mouseEvent) {
        Mail mail = new Mail();
        mail.setFrom(this.tf_from.getText());
        mail.setTo(this.tf_to.getText());
        mail.setSubject(this.tf_subject.getText());
        mail.setContent(this.ta_content.getText());
        List<Mail> mails = new ArrayList<>();
        mails.add(mail);
        System.out.println(mail);

        MailSender mailSender = MailSender.getInstance();
        mailSender.send(mails);
    }
}
