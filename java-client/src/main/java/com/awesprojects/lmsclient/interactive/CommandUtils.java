package com.awesprojects.lmsclient.interactive;

import com.awesprojects.lmsclient.LMSClient;
import com.awesprojects.lmsclient.api.Users;
import org.json.JSONException;

public class CommandUtils {

    private final LMSClient client;
    
    public boolean login(){
        client.out.print("username : ");
        String id = scanner.nextLine();
        client.out.print("password : ");
        String password = scanner.nextLine();
        try {
            int userId = Integer.parseInt(id);
            String response = Users.getAccessToken(userId, password);
            return true;
        }catch(NumberFormatException nfe){
            client.err.println("looks like it is invalid id");
        }catch(IndexOutOfBoundsException iobe){
            client.err.println("id can't be too long, try again");
        }catch (JSONException jsone){
            client.err.println("error happen while parsing server's response");
        }catch(Throwable t){
            client.err.println("login error: ");
            t.printStackTrace(client.err);
        }
        return false;
    }

}
