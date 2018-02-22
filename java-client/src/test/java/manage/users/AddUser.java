package manage.users;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.Librarian;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class AddUser {

    public static void main(String[] args){
        AccessToken accessToken = Config.accessToken;
        Librarian user = new Librarian();
        user.setName("java test user");
        user.setAddress("address");
        user.setPhone("phone");
        Responsable responsable = ManageAPI.Users.addUser(accessToken,user);
        System.out.println(responsable);
    }
}
