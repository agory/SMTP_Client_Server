package Model.Method;

import Model.Connexion;

public abstract class Methods {
    //FIELDS
    Connexion server;
    private String command;

    //CONSTRUCTEUR
    Methods(Connexion server, String command) {
        this.server = server;
        this.command = command;
    }

    //GETTER SETTER
    public String getCommand() {
        return command;
    }

    //METHODS
    public void answerCommand(String content)
    {
        server.sendResponse(makeAnswer(content));
    }

    //ABSTRACT
    abstract String makeAnswer(String content);
    abstract String[] extractContent(String content);

}
