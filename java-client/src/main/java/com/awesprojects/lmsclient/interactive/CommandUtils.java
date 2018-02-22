package com.awesprojects.lmsclient.interactive;

import com.awesprojects.lmsclient.LMSClient;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsed;
import org.json.JSONException;

import java.util.Scanner;

public class CommandUtils {

    private final LMSClient client;

    public CommandUtils(LMSClient client){
        this.client = client;
    }

    public boolean login(Scanner scanner){
        client.out.print("username : ");
        String id = scanner.nextLine();
        client.out.print("password : ");
        String password = scanner.nextLine();
        try {
            int userId = Integer.parseInt(id);
            Responsed response = UsersAPI.getAccessToken(userId, password);
            if (response instanceof AccessToken)
                return true;
            if (response instanceof Response)
                if (((Response) response).isError()) {
                    client.err.println("server returned error response : "+response.toString());
                    return false;
                }
            return false;
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
