package model.method;

import model.Connexion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.parser.JSONParser;

/**
 *
 * Created by jeremy on 03/04/2017.
 */
public class MethodsDATA extends Methods {
    String subject="";
    String date="";

    public MethodsDATA(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isState(server.STATE_MAILDATA))
        {
            server.sendResponse("354 Start mail input; end with <CRLF>.<CRLF>");
            String data = readStream();
            writeDataOnServer(data);
            server.setState(server.STATE_AUTHENTIFICATION);
            return "250 OK";
        }
        return "";
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }

    private String readStream(){
        String allLines = "";
        String line = "";
        int num;
        char ch;
        int subject = 0;
        int date = 0;

        while(true)
        {
            try {
                num = server.getInput().read();
                ch = (char)num;
                allLines += ch;
                line += ch;

                if (subject==1 && date==0)
                {
                    this.date += ch;
                    if (line.contains("\r\n"))
                    {
                        date = 1;
                        line="";
                    }
                }

                if (subject==0)
                {
                    this.subject += ch;
                    if (line.contains("\r\n"))
                    {
                        subject = 1;
                        line="";
                    }
                }



                if(allLines.contains("\r\n.\r\n"))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    public void writeDataOnServer(String data){
        List<String> usersFiles = server.getRcptUsersFile();
        JSONParser parser = new JSONParser();

        for (int i = 0; i < usersFiles.size(); i++) {
            try {
                Object obj = parser.parse(new FileReader("resources\\users\\" + usersFiles.get(i)));
                JSONObject jsonObject = (JSONObject) obj;
                int nbMessages = Integer.parseInt(jsonObject.get("number_messages").toString());
                JSONArray messages = (JSONArray) jsonObject.get("messages");

                nbMessages++;
                jsonObject.put("number_messages",nbMessages);
                //add
                JSONObject mes = new JSONObject();
                mes.put("from", server.getRcptUsers());
                mes.put("to", server.getRcptUsers().get(i));
                mes.put("subject", this.subject);
                mes.put("date", this.date);
                mes.put("marked", false);
                mes.put("message-id", nbMessages);
                mes.put("content", data);

                messages.add(mes);

                try (FileWriter file = new FileWriter("resources\\users\\" + usersFiles.get(i))) {
                    file.write(jsonObject.toJSONString());
                    System.out.println("Successfully Copied JSON Object to File...");
                    System.out.println("\nJSON Object: " + jsonObject);
                }
            } catch (ParseException | IOException e) {
                return;
            }
        }
    }
}
