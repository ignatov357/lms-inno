package users;

import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class GetUserInfo {

    public static void main(String[] args){
        Responsable r = UsersAPI.getUserInfo(Config.librarianAccessToken);
        System.out.println(r.toString());
    }
}
