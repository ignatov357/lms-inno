package users;

import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class GetCheckedOutDocuments {

    public static void main(String[] args){
        Responsable responsable = UsersAPI.getCheckedOutDocuments(Config.patronAccessToken);
        System.out.println(responsable);
    }

}
