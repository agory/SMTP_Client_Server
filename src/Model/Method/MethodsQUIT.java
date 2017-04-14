package Model.Method;

import Model.Connexion;

public class MethodsQUIT extends Methods {

    public MethodsQUIT(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isState(server.STATE_MAILDATA) || server.isState(server.STATE_CONNECTION)){
            server.setClose();
            return "250 OK";
        }
        return "";
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }
}
