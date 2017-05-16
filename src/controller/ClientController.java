package controller;

import client.MailSender;
import client.exception.SMTPException;
import client.exception.UnknowException;
import client.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
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
        mail.setFrom(this.tf_from.getText().trim());
        for (String to : this.tf_to.getText().split(";")) {
            mail.addTo(to.trim());
        }
        mail.setSubject(this.tf_subject.getText().trim());
        mail.setContent(this.ta_content.getText().trim());
        List<Mail> mails = new ArrayList<>();
        mails.add(mail);
        System.out.println(mail);

        MailSender mailSender = MailSender.getInstance();
        try {
            mailSender.send(mails);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Message send");

            alert.showAndWait();
        } catch (UnknowException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Unknown User");
            alert.setHeaderText(e.getMessage());

            alert.showAndWait();
        } catch (SMTPException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Request failed");
            alert.setHeaderText(e.getMessage());

            alert.showAndWait();
        }

        tf_subject.setText("");
        tf_to.setText("");
        tf_from.setText("");
        ta_content.setText("");
    }
}
