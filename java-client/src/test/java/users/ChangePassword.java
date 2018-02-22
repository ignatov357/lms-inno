package users;

import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

public class ChangePassword {

    public static void main(String[] args){
        Responsable r = UsersAPI.changePassword(new AccessToken(
                "69bbc8253aa92d7e36968101882729a827ebae86c74ead3a015bd3fee1918a17",0l),
                "1234567890");
        System.out.println(r);
    }

}
