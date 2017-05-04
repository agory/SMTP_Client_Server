package model;

import model.method.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Connexion implements Runnable {

    //FIELD
    private BufferedReader input;
    private DataOutputStream output;
    private Socket clientSocket;
    private boolean close;
    private String userfile;
    private List<String> rcptUsersFile;
    private List<String> rcptUsers;
    private String mailOwner;

    //CONSTANT
    private final String STATE_CLOSE = "close";
    public final String STATE_CONNECTION = "connection";
    public final String STATE_AUTHENTIFICATION = "authentification";
    public final String STATE_MAILTARGET = "mailtarget";
    public final String STATE_MAILDATA = "maildata";
    private String state = STATE_CLOSE;

    private ArrayList<Methods> methodsList;

    //INIT
    private void setMethodsList(){
        methodsList.add(new MethodsHELO(this,"HELO"));
        methodsList.add(new MethodsHELO(this,"EHLO"));
        methodsList.add(new MethodsMAIL(this,"MAIL FROM"));
        methodsList.add(new MethodsRCPT(this,"RCPT TO"));
        methodsList.add(new MethodsDATA(this,"DATA"));
        methodsList.add(new MethodsREST(this,"RSET"));
        methodsList.add(new MethodsQUIT(this,"QUIT"));
    }

    //CONSTRUCTOR
    Connexion(Socket aClientSocket){
        methodsList = new ArrayList<>();
        setMethodsList();
        try {
            clientSocket = aClientSocket;
            input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            output =new DataOutputStream( clientSocket.getOutputStream());
            rcptUsersFile = new ArrayList<>();
            rcptUsers = new ArrayList<>();
        }
        catch(IOException e) {
            System.out.println("Connection: "+e.getMessage());
        }
    }

    //GETTER & SETTER
    public void setState(String state){ this.state = state; }
    public BufferedReader getInput() {
        return input;
    }
    public void setClose() {
        this.close = true;
    }
    public void setUserFile(String file) {
        this.userfile = file;
    }
    public String getUserfile() {
        return userfile;
    }
    public List<String> getRcptUsers() {
        return rcptUsers;
    }
    public void setMailOwner(String mailOwner) {
        this.mailOwner = mailOwner;
    }
    public List<String> getRcptUsersFile() {
        return rcptUsersFile;
    }

    //RUN
    public void run(){
        try {
            int code = 220;
            String content = "Polytech.com Simple Mail Transfer Service Ready";
            String data = code + " " + content + "\r\n";
            System.out.println ("New connection: " + clientSocket.getPort() + ", " + clientSocket.getInetAddress());

            output.writeBytes(data);
            output.flush();
            System.out.println ("send: " + data);

            state = STATE_CONNECTION;
            if(clientSocket.isConnected())
                readCommand();
        }
        catch(EOFException e) {
            System.err.println("EOF: "+e.getMessage()); }
        catch(IOException e) {
            System.err.println("IO: "+e.getMessage());}
    }

    //METHODS
    private void readCommand(){
        System.out.println("Reading from stream:");
        try {
            String command;
            while ((command = input.readLine()) != null && !close) {
                if(!Objects.equals(command, ""))
                {
                    System.out.println ("receive from : " + clientSocket.getInetAddress() + " : " + clientSocket.getPort() + ", command : " + command);
                    answerCommand(command);
                }
                if(close)
                    break;
            }
            if(close)
            {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void answerCommand(String data){
        String command = data.split("\\s+")[0];
        command = command.toUpperCase();

        if(Objects.equals(command, "MAIL") || Objects.equals(command, "RCPT"))
            command += " " + data.split("\\s+")[1];

        String content = "";
        for (String s : data.split("\\s+"))
            content += s + " ";

        for (Methods method : methodsList)
        {
            if(Objects.equals(method.getCommand(), command))
            {
                method.answerCommand(content);
                return;
            }
        }
        sendResponse("-ERR unknown command");
    }

    public void sendResponse(String data){
        data += "\r\n";
        System.out.println("send: " + data);
        try {
            output.writeBytes(data);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isState(String isState){
        return Objects.equals(state, isState);
    }

    public void reset() {
        state = STATE_CONNECTION;
        getRcptUsers().clear();
        getRcptUsersFile().clear();
        mailOwner = "";
        userfile = "";
    }

//    public boolean isMailTarget(){
//        return Objects.equals(state, STATE_MAILTARGET);
//    }
//
//    public boolean isMailData() {
//        return Objects.equals(state, STATE_MAILDATA);
//    }
}
