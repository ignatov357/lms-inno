import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsed;

public class GetAccessToken {

    public static void main(String[] args){
        Responsed accessToken = UsersAPI.getAccessToken(Integer.parseInt(args[0]),args[1]);
        System.out.println(accessToken);
        if (accessToken instanceof AccessToken)
            System.out.println("access token will expire "+((AccessToken) accessToken).getExpirationDateInString());
    }

}
