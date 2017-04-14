package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    protected boolean serverIsRunning = false;
    private ArrayList<Connexion> connections = new ArrayList<>();

    private void tcpConnection()
    {
        try{
            int serverPort = 110;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            System.err.println("server start listening...");

            while(serverIsRunning) {
                Socket clientSocket = listenSocket.accept();
                if(serverIsRunning)
                {
                    Connexion connection = new Connexion(clientSocket);
                    connections.add(connection);
                    new Thread(connection).start();
                }
            }
        }
        catch(IOException e) {
            System.err.println("Listen :"+e.getMessage());}
    }

    public boolean isServerIsRunning() {
        return serverIsRunning;
    }

    public void setServerIsRunning(boolean serverIsRunning) {
        this.serverIsRunning = serverIsRunning;
    }

    @Override
    public void run() {
        tcpConnection();
    }


    private static Server server;

    protected Server(){}

    public static synchronized Server getInstance( ) {
        if (server == null)
            server=new Server();
        return server;
    }

    public void cutConnection() {
        for(Connexion connection : connections)
        {
            connection.setClose();
        }
    }
}
