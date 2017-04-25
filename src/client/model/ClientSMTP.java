package client.model;

import client.exception.NotAllowedMethodException;
import client.exception.RequestFailedException;
import client.exception.UnknowException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

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
        this.state = ClientState.Disconnected;
    }

    public void run() {
        try {
            System.out.println("Start SMTP CLient");
            this.socket = new Socket(this.ip, port);
            System.out.println("Start tcp connexion : " + this.ip.getHostAddress() + " : " + this.port);
            this.state = ClientState.Connected;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ehlo(String domainName) throws NotAllowedMethodException, RequestFailedException {
        if (!state.equals(ClientState.Connected))
            throw new NotAllowedMethodException("ehlo can be used only on connected state");

        this.write("EHLO " + domainName);
        Response response = this.read();
        if (response.getCode() == 220)
            this.state = ClientState.Authentification;
        else
            throw new RequestFailedException();
    }

    public void rset() throws NotAllowedMethodException, RequestFailedException {
        if (!(state.equals(ClientState.Authentification) || state.equals(ClientState.MailTarget)|| state.equals(ClientState.MailData)))
            throw new NotAllowedMethodException("rset can be used only on authenfication or MailTarget or MailData state");
        this.write("RSET");
        Response response = this.read();
        if (response.getCode() == 250)
            this.state = ClientState.Authentification;
        else
            throw new RequestFailedException();

    }

    public void mail(String from) throws NotAllowedMethodException, RequestFailedException, UnknowException {
        if (!state.equals(ClientState.Authentification))
            throw new NotAllowedMethodException("mail can be used only on Authentification state");
        this.write("MAIL " + from);
        Response response = this.read();
        if (response.getCode() == 250)
            this.state = ClientState.MailTarget;
        else
            throw new RequestFailedException();

    }


    public void rcpt(String dest) throws NotAllowedMethodException, RequestFailedException, UnknowException {
        if (!state.equals(ClientState.MailTarget))
            throw new NotAllowedMethodException("rcpt can be used only on MailTarget state");
        this.write("RCPT " + dest);
        Response response = this.read();
        if (response.getCode() == 550)
            throw new UnknowException("No existing email");
        else if (response.getCode() != 250)
            throw new RequestFailedException();

    }

    public void data(String content) throws NotAllowedMethodException, RequestFailedException {
        if (!state.equals(ClientState.MailTarget))
            throw new NotAllowedMethodException("data can be used only on MailTarget state");
        this.write("DATA");
        Response response = this.read();
        if (response.getCode() != 354)
            throw new RequestFailedException();
        this.write(content);
        response = this.read();
        if(response.getCode() != 250)
            throw new RequestFailedException();
        this.state = ClientState.MailData;
    }

    public void quit() throws NotAllowedMethodException, RequestFailedException {
        if (!(state.equals(ClientState.MailData) || state.equals(ClientState.Authentification)))
            throw new NotAllowedMethodException("quit can be used only on MailData or Connected state");
        this.write("QUIT");
        Response response = this.read();
        if (response.getCode() == 250) {
            this.state = ClientState.Authentification;
        }
        this.close();

    }


    /*
    * method input output
    * */

    private void write(String data) {
        data += "\r\n";
        System.out.print("send : \r\n" + data);
        try {
            OutputStream output = this.socket.getOutputStream();
            output.write(data.getBytes());
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private Response read() throws RequestFailedException {
        try {
            BufferedReader buffer = new BufferedReader( new InputStreamReader(this.socket.getInputStream()));
            String command;
            while ((command = buffer.readLine()) != null && !this.socket.isClosed()) {
                if(!Objects.equals(command, ""))
                {
                    System.out.println("receive : \r\n" + command);
                    return new Response(command);
                }
                if(this.socket.isClosed())
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RequestFailedException("Not Assessable request");
        } catch (IOException e) {
            throw new RequestFailedException("Connection lost");
        }
        throw new RequestFailedException("Connection lost");

    }

    /*private Response readt() throws RequestFailedException {
        byte[] buffer = new byte[8192]; //config?
        int count = 0;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
            while ((count = bufferedInputStream.read(buffer)) <= 0) {
            }
            String raw = new String(buffer, "UTF-8");
            System.out.println("receive : \r\n" + raw);
            return new Response(raw);
        } catch (UnsupportedEncodingException e) {
            throw new RequestFailedException("Not Assessable request");
        } catch (IOException e) {
            throw new RequestFailedException("Connection lost");
        }

    }*/

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

