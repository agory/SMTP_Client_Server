package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private TextField ta_content;


    public void send(MouseEvent mouseEvent) {
        System.out.println("plop");
    }
}
