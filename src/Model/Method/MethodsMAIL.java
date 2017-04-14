package Model.Method;

import Model.Connexion;

/**
 *
 * Created by jeremy on 03/04/2017.
 */
public class MethodsMAIL extends Methods {
    public MethodsMAIL(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if(server.isState(server.STATE_AUTHENTIFICATION))
        {
            String from = extractContent(content)[0]; //Attention si c'est vide
            server.setMailOwner(from);
            server.setState(server.STATE_MAILTARGET);
            return "250 OK";
        }
        return "you must be connected"; //A ameliorer
    }

    @Override
    String[] extractContent(String content) {
        return new String[]{content.split("\\s+")[1]};
    }
}
