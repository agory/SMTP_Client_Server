package model.method;

import model.Connexion;

/**
 *
 * Created by jeremy on 03/04/2017.
 */
public class MethodsHELO extends Methods {

    public MethodsHELO(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isState(server.STATE_CONNECTION)){
            server.setState(server.STATE_AUTHENTIFICATION);
            return "250 -Polytech.com greats " + extractContent(content)[0];
        }
        return "";
    }

    @Override
    String[] extractContent(String content) {
        return new String[]{content.split("\\s+")[1]};
    }
}
