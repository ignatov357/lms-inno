package users;

import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

public class GetAccessToken {

    public static void main(String[] args){
        Responsable accessToken = UsersAPI.getAccessToken(1,"1234567890");
        System.out.println(accessToken);
        if (accessToken instanceof AccessToken)
            System.out.println("access token will expire "+((AccessToken) accessToken).getExpirationDateInString());
    }

}
