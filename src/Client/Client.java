package Client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by gorya on 03/04/2017.
 */
public class Client {

    private Socket socket;
    private InetAddress ip;
    private int port;
    private ClientState state;

    public Client(InetAddress ip,int port) {
        this.port = port;
        this.ip = ip;
    }

    public static void main(String[] arg) {
        try {
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            Client client = new Client(ip,25);
            client.run();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            this.socket = new Socket(this.ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ehlo(String domainName) throws NotAllowedMethodException {
        if(!state.equals(ClientState.Connected))
            throw new NotAllowedMethodException("rset can be used only on connected state");

    }

    public void rset() throws NotAllowedMethodException {
        if(!(state.equals(ClientState.Authentification) || state.equals(ClientState.MailTarget)))
            throw new NotAllowedMethodException("rset can be used only on authenfication or MailTarget state");
        this.write("RSET");
        try {
            String response = this.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mail(String from) throws NotAllowedMethodException {
        if(!state.equals(ClientState.Authentification))
            throw new NotAllowedMethodException("rset can be used only on Authentification state");
        this.write("MAIL FROM:<"+from+">");
    }


    public void rcpt(String dest) throws NotAllowedMethodException {
        if(!state.equals(ClientState.MailTarget))
            throw new NotAllowedMethodException("rset can be used only on MailTarget state");
        this.write("RCPT TO:<"+dest+">");
    }

    public void data(String content) throws NotAllowedMethodException {
        if(!state.equals(ClientState.MailTarget))
            throw new NotAllowedMethodException("rset can be used only on MailTarget state");
        this.write("DATA");
        try {
            String response = this.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit() throws NotAllowedMethodException {
        if(!(state.equals(ClientState.MailData) || state.equals(ClientState.Connected)))
            throw new NotAllowedMethodException("rset can be used only on MailData or Connected state");
        this.write("QUIT");
    }


    /*
    * method input output
    * */

    private void write(String data) {
        data += "\r\n";
        try {
            OutputStream output = this.socket.getOutputStream();
            output.write(data.getBytes());
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String read() throws Exception {
        byte[] buffer = new byte[8192]; //config?
        int count = 0;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
            while ((count = bufferedInputStream.read(buffer)) <= 0) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(buffer, "UTF-8");
    }
}

