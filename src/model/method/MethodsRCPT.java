package model.method;

import model.Connexion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * Created by jeremy on 03/04/2017.
 */
public class MethodsRCPT extends Methods {

    private int nbrMsg;

    public MethodsRCPT(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isState(server.STATE_MAILTARGET) ||server.isState(server.STATE_MAILDATA) )
        {
            String[] user = extractContent(content);
            if(checkRcpt(user))
            {
                server.setState(server.STATE_MAILDATA);
                return "250 OK";
            }
            return "550 No such user here";
        }
        return "";
    }

    @Override
    String[] extractContent(String content) {
        return new String[]{content.split("\\s+")[2]};
    }

    private boolean checkRcpt(String[] contentSend){
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("resources\\users.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray users = (JSONArray) jsonObject.get("users");
            for (Object user : users) {
                JSONObject slide = (JSONObject) user;
                String username = (String) slide.get("username");
                String file = (String) slide.get("file");

                if(Objects.equals(username, contentSend[0]))
                {
//                    setUserFile(file);
//                    setNumberOfMessages();
                    server.getRcptUsersFile().add(file);
                    server.getRcptUsers().add(username);
                    return true;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    private void setUserFile(String file) {
        server.setUserFile(file);
    }

    private void setNumberOfMessages()
    {
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("resources\\users\\" + server.getUserfile()));
            JSONObject jsonObject = (JSONObject) obj;
            nbrMsg = Integer.parseInt(String.valueOf(jsonObject.get("number_messages")));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
