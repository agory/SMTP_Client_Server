package model.method;

import model.Connexion;

/**
 * Created by jeremy on 03/04/2017.
 */
public class MethodsREST extends Methods {
    public MethodsREST(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isState(server.STATE_AUTHENTIFICATION) || server.isState(server.STATE_MAILTARGET)) {
            server.reset();
            return "250 OK";
        }
        return "";
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }
}
