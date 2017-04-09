package Client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by gorya on 03/04/2017.
 */
public class ClientSMTP implements Runnable {

    private Socket socket;
    private InetAddress ip;
    private int port;
    private ClientState state;

    public ClientSMTP(InetAddress ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public void run() {
        try {
            this.socket = new Socket(this.ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ehlo(String domainName) throws NotAllowedMethodException, RequestFailedException {
        if (!state.equals(ClientState.Connected))
            throw new NotAllowedMethodException("ehlo can be used only on connected state");

        this.write("EHLO " + domainName);
        Response response = this.read();
        if(response.getCode() == 250) {
            this.state = ClientState.Authentification;
        }
    }

    public void rset() throws NotAllowedMethodException, RequestFailedException  {
        if (!(state.equals(ClientState.Authentification) || state.equals(ClientState.MailTarget)))
            throw new NotAllowedMethodException("rset can be used only on authenfication or MailTarget state");
        this.write("RSET");
        Response response = this.read();
        if(response.getCode() == 250) {
            this.state = ClientState.Authentification;
        }

    }

    public void mail(String from) throws NotAllowedMethodException, RequestFailedException, UnknowException  {
        if (!state.equals(ClientState.Authentification))
            throw new NotAllowedMethodException("mail can be used only on Authentification state");
        this.write("MAIL FROM:<" + from + ">");
        Response response = this.read();
        if(response.getCode() == 250) {
            this.state = ClientState.MailTarget;
        } else {
            throw new UnknowException("No existing email");
        }
    }


    public void rcpt(String dest) throws NotAllowedMethodException, RequestFailedException  {
        if (!state.equals(ClientState.MailTarget))
            throw new NotAllowedMethodException("rcpt can be used only on MailTarget state");
        this.write("RCPT TO:<" + dest + ">");
        Response response = this.read();
        if(response.getCode() == 250) {
        }
    }

    public void data(String content) throws NotAllowedMethodException, RequestFailedException {
        if (!state.equals(ClientState.MailTarget))
            throw new NotAllowedMethodException("data can be used only on MailTarget state");
        this.write("DATA");
        Response response = this.read();
        if(response.getCode() == 250) {
            this.state = ClientState.MailData;
        }
    }

    public void quit() throws NotAllowedMethodException, RequestFailedException {
        if (!(state.equals(ClientState.MailData) || state.equals(ClientState.Connected)))
            throw new NotAllowedMethodException("quit can be used only on MailData or Connected state");
        this.write("QUIT");
        Response response = this.read();
        if(response.getCode() == 250) {
            this.state = ClientState.Authentification;
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private Response read() throws RequestFailedException {
        byte[] buffer = new byte[8192]; //config?
        int count = 0;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
            while ((count = bufferedInputStream.read(buffer)) <= 0) {
            }
            String raw = new String(buffer, "UTF-8");
            return new Response(raw);
        } catch (UnsupportedEncodingException e) {
            throw new RequestFailedException("Not Assessable request");
        } catch (IOException e) {
            throw new RequestFailedException("Connection lost");
        }

    }
}

