package manage.users;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class GetDocumentsUserCheckedOut {

    public static void main(String[] args){
        AccessToken accessToken = Config.accessToken;
        Responsable[] documents = ManageAPI.Users.getDocumentsUserCheckedOut(accessToken,1);
        if (documents instanceof Document[]){
            for (Responsable d : documents){
                System.out.println(d);
            }
        }else {
            System.out.println(documents[0]);
        }
    }

}
