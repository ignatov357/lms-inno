package users;

import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

public class GetAccessToken {

    public static void main(String[] args){
        Responsable accessToken = UsersAPI.getAccessToken(Integer.parseInt(args[0]),args[1]);
        System.out.println(accessToken);
        if (accessToken instanceof AccessToken)
            System.out.println("access token will expire "+((AccessToken) accessToken).getExpirationDateInString());
    }

}
