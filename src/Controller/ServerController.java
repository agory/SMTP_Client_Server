package Controller;

import Model.Console;
import Model.Server;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.PrintStream;

public class ServerController {
    @FXML
    private Button btn_switchOnOff;
    @FXML
    private TextArea tArea_out;
    private PrintStream ps;

    public void initialize() {
        ps = new PrintStream(new Console(tArea_out));
        tArea_out.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                tArea_out.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
    }

    public void startServer(MouseEvent mouseEvent) {
        System.setOut(ps);
        System.setErr(ps);
        Server server = Server.getInstance();
        if (!server.isServerIsRunning())
        {
            server.setServerIsRunning(true);
            new Thread(server).start();
            btn_switchOnOff.setText("On");
        }
        else
        {
            server.setServerIsRunning(false);
            server.cutConnection();
            btn_switchOnOff.setText("Off");
            System.err.println("Server Off");
        }

    }
}
