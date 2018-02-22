package manage.users;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class GetDocumentsUserCheckedOut {

    public static void main(String[] args){
        AccessToken accessToken = Config.accessToken;
        Responsable responsable = ManageAPI.Users.getDocumentsUserCheckedOut(accessToken,1);
        System.out.println(responsable);
    }

}
