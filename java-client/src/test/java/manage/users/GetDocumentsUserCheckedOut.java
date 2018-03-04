package manage.users;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class GetDocumentsUserCheckedOut {

    public static void main(String[] args){
        AccessToken accessToken = Config.accessToken;
        Responsable documents = ManageAPI.Users.getDocumentsUserCheckedOut(accessToken,1);
        if (documents instanceof ResponsableContainer){
            for (Responsable d : ((ResponsableContainer<Document[]>) documents).get()){
                System.out.println(d);
            }
        }else {
            System.out.println(documents);
        }
    }

}
